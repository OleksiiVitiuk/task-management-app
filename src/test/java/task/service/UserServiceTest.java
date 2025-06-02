package task.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import task.dto.UserLoginDto;
import task.dto.UserRegistrationRequestDto;
import task.dto.UserResponseDto;
import task.dto.UserResponseLoginDto;
import task.security.AuthenticationService;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;


    @Test
    @DisplayName("Should register user and return UserResponseDto")
    @Sql(scripts = "classpath:database/user/delete-users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/user/delete-user-with-id-1.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void register_validRequest_returnsUserResponseDto() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        requestDto.setEmail("test@gmail.com");
        requestDto.setUsername("test");
        requestDto.setPassword("testPassword123");
        requestDto.setRepeatPassword("testPassword123");
        requestDto.setFirstName("user");
        requestDto.setLastName("user");

        UserResponseDto response = userService.register(requestDto);

        assertEquals("test@gmail.com", response.getEmail());
        assertEquals("test", response.getUsername());
    }

    @Test
    @DisplayName("Should login user and return JWT token")
    @Sql(scripts = "classpath:database/user/create-user.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/user/delete-user-with-id-1.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void login_validCredentials_returnsJwtToken() {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setUsername("user");
        loginDto.setPassword("testPassword123");

        UserResponseLoginDto response = authenticationService.authenticate(loginDto);

        assertNotNull(response.token());
        assertTrue(response.token().startsWith("eyJ"));
    }
}
