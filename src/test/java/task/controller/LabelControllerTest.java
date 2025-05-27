package task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import task.dto.LabelCreateRequestDto;
import task.model.Label;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
class LabelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Create label - success")
    @Sql(scripts = {"classpath:database/user/create-user.sql",
            "classpath:database/project/create-project.sql",
            "classpath:database/task/create-task.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/label/delete-label-with-id-1.sql",
            "classpath:database/task/delete-task-with-id-1.sql",
            "classpath:database/project/delete-project-with-id-1.sql",
            "classpath:database/user/delete-user-with-id-1.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createLabelTest_createLabel_ok() throws Exception {
        LabelCreateRequestDto request = new LabelCreateRequestDto();
        request.setTaskId(1L);
        request.setName("Urgent");
        request.setColor(Label.Color.RED);

        mockMvc.perform(post("/labels")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Urgent"))
                .andExpect(jsonPath("$.color").value("RED"));
    }

    @Test
    @DisplayName("Update label - success")
    @Sql(scripts = {"classpath:database/user/create-user.sql",
            "classpath:database/project/create-project.sql",
            "classpath:database/task/create-task.sql",
            "classpath:database/label/create-label.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/label/delete-label-with-id-1.sql",
            "classpath:database/task/delete-task-with-id-1.sql",
            "classpath:database/project/delete-project-with-id-1.sql",
            "classpath:database/user/delete-user-with-id-1.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateLabelTest_updateLabel_ok() throws Exception {
        LabelCreateRequestDto request = new LabelCreateRequestDto();
        request.setTaskId(1L);
        request.setName("Updated");
        request.setColor(Label.Color.GREEN);

        mockMvc.perform(put("/labels/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.color").value("GREEN"));
    }

    @Test
    @DisplayName("Get labels - success")
    @WithMockUser(roles = "USER")
    @Sql(scripts = {"classpath:database/user/create-user.sql",
            "classpath:database/project/create-project.sql",
            "classpath:database/task/create-task.sql",
            "classpath:database/label/create-label.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/label/delete-label-with-id-1.sql",
            "classpath:database/task/delete-task-with-id-1.sql",
            "classpath:database/project/delete-project-with-id-1.sql",
            "classpath:database/user/delete-user-with-id-1.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getLabelsTest_getLabels_ok() throws Exception {
        mockMvc.perform(get("/labels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id")
                        .value(1L))
                .andExpect(jsonPath("$.content[0].name")
                        .value("label"))
                .andExpect(jsonPath("$.content[0].color")
                        .value("RED"));
    }

    @Test
    @DisplayName("Delete label - success")
    @Sql(scripts = {"classpath:database/user/create-user.sql",
            "classpath:database/project/create-project.sql",
            "classpath:database/task/create-task.sql",
            "classpath:database/label/create-label.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/task/delete-task-with-id-1.sql",
            "classpath:database/project/delete-project-with-id-1.sql",
            "classpath:database/user/delete-user-with-id-1.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteLabelTest_deleteTest_ok() throws Exception {
        mockMvc.perform(delete("/labels/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Create label - validation fail (short name)")
    void createLabelTest_validationFail_notOk() throws Exception {
        LabelCreateRequestDto request = new LabelCreateRequestDto();
        request.setTaskId(1L);
        request.setName("ab");
        request.setColor(Label.Color.GREEN);

        mockMvc.perform(post("/labels")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
