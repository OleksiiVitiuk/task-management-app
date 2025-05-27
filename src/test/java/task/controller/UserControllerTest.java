package task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import task.dto.UserResponseDto;
import task.dto.UserUpdateRequestDto;
import task.dto.UserWithRoleDto;
import task.model.Role;
import task.model.User;
import task.util.TestUtil;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", roles = {"USER", "ADMIN"})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Update user — using real User principal")
    @Sql(scripts = "classpath:database/user/create-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/user/delete-user-with-id-1.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateUserTest_updateUser_ok() throws Exception {
        User customUser = TestUtil.getUser();

        TestingAuthenticationToken auth = new TestingAuthenticationToken(customUser, null, "ROLE_USER");
        auth.setAuthenticated(true);

        SecurityContextHolder.getContext().setAuthentication(auth);

        UserUpdateRequestDto expected = TestUtil.getUserUpdateRequestDto();

        String json = objectMapper.writeValueAsString(expected);

        MvcResult result = mockMvc.perform(patch("/users/me")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserResponseDto.class);

        assertUser(actual, expected.getUsername(), expected.getEmail(),
                expected.getFirstName(), expected.getLastName());
    }

    private void assertUser(UserResponseDto response,
                            String name, String email,
                            String firstName, String lastName) {
        assertEquals(name, response.getUsername());
        assertEquals(email, response.getEmail());
        assertEquals(firstName, response.getFirstName());
        assertEquals(lastName, response.getLastName());
    }


    @Test
    @DisplayName("Get current user info")
    @Sql(scripts = "classpath:database/user/create-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/user/delete-user-with-id-1.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUser_getUserTest_ok() throws Exception {
        User expected = TestUtil.getUser();

        TestingAuthenticationToken auth = new TestingAuthenticationToken(expected, null, "ROLE_USER");
        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);

        MvcResult result = mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andReturn();

        UserWithRoleDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserWithRoleDto.class);

        assertUserWithRole(actual, expected.getUsername(), expected.getEmail(),
                expected.getFirstName(), expected.getLastName(), expected.getRoles());
    }

    private void assertUserWithRole(UserWithRoleDto response,
                                    String name, String email,
                                    String firstName, String lastName,
                                    Set<Role> roles) {
        assertEquals(name, response.getUsername());
        assertEquals(email, response.getEmail());
        assertEquals(firstName, response.getFirstName());
        assertEquals(lastName, response.getLastName());
        assertEquals(response.getRoleNames(),
                roles.stream().map(String::valueOf).collect(Collectors.toSet()));
    }

}
