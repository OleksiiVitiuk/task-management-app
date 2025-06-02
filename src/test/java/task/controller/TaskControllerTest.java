package task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import task.dto.TaskCreateRequestDto;
import task.dto.TaskDto;
import task.model.Status;
import task.model.Task;
import task.service.EmailService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", roles = {"USER", "ADMIN"})
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeAll() {
        doNothing().when(emailService).sendEmail(anyString(), anyString());
    }

    @Test
    @DisplayName("Create task")
    @Sql(scripts = {
            "classpath:database/project/create-project.sql",
            "classpath:database/user/create-user.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/task/delete-task-with-id-1.sql",
            "classpath:database/project/delete-project-with-id-1.sql",
            "classpath:database/user/delete-user-with-id-1.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createTaskTest_postRequest_responseCreated() throws Exception {
        TaskCreateRequestDto expect = new TaskCreateRequestDto();
        expect.setName("New Task");
        expect.setDescription("New description");
        expect.setPriority(Task.Priority.HIGH);
        expect.setStatus(Status.IN_PROGRESS);
        expect.setDueDate(LocalDate.of(2025, 5, 31));
        expect.setProjectId(1L);
        expect.setAssigneeId(1L);

        String json = objectMapper.writeValueAsString(expect);

        MvcResult result = mockMvc.perform(post("/tasks")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        TaskDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), TaskDto.class);

        assertTask(actual, 1L,
                expect.getName(),
                expect.getDescription(),
                expect.getPriority().toString(),
                expect.getStatus().toString(),
                expect.getDueDate().toString(),
                expect.getProjectId(),
                expect.getAssigneeId());
    }

    @Test
    @DisplayName("Get task")
    @Sql(scripts = {"classpath:database/user/create-user.sql",
            "classpath:database/project/create-project.sql",
            "classpath:database/task/create-task.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/task/delete-task-with-id-1.sql",
            "classpath:database/project/delete-project-with-id-1.sql",
            "classpath:database/user/delete-user-with-id-1.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getTaskTest_getTaskRequest_responseOk() throws Exception {
        MvcResult result = mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andReturn();

        TaskDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), TaskDto.class);

        assertTask(actual, 1L,
                "task",
                "task desc",
                "LOW",
                "IN_PROGRESS",
                "2025-05-27",
                1L,
                1L);
    }

    @Test
    @DisplayName("Get task - not found")
    public void getTaskTest_nonExistingId_responseNotFound() throws Exception {
        mockMvc.perform(get("/tasks/999"))
                .andExpect(status().isNotFound());
    }

    private void assertTask(TaskDto dto,
                            Long id,
                            String name,
                            String description,
                            String priority,
                            String status,
                            String dueDate,
                            Long projectId,
                            Long assigneeId) {
        assertEquals(id, dto.id());
        assertEquals(name, dto.name());
        assertEquals(description, dto.description());
        assertEquals(priority, dto.priority());
        assertEquals(status, dto.status());
        assertEquals(dueDate, dto.dueDate().toString());
        assertEquals(projectId, dto.projectId());
        assertEquals(assigneeId, dto.assigneeId());
    }

}
