package task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.web.multipart.MultipartFile;
import task.dto.AttachmentDto;
import task.service.DropboxService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", roles = {"USER"})
class AttachmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DropboxService dropboxService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        when(dropboxService.uploadFile(any(MultipartFile.class))).thenReturn("dropbox-file-id");
        doNothing().when(dropboxService).downloadById(anyString());
        doNothing().when(dropboxService).deleteFile(anyString());
    }

    @Test
    @DisplayName("Upload attachment")
    @Sql(scripts = {
            "classpath:database/user/create-user.sql",
            "classpath:database/project/create-project.sql",
            "classpath:database/task/create-task.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/attachment/delete-attachment-with-id-1.sql",
            "classpath:database/task/delete-task-with-id-1.sql",
            "classpath:database/project/delete-project-with-id-1.sql",
            "classpath:database/user/delete-user-with-id-1.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void uploadAttachment_returnsCreated() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Some content".getBytes());

        MvcResult result = mockMvc.perform(multipart("/attachments")
                        .file(file)
                        .param("taskId", "1")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andReturn();

        AttachmentDto response = objectMapper.readValue(
                result.getResponse().getContentAsString(), AttachmentDto.class);
        assertEquals("test.txt", response.filename());
        assertEquals("dropbox-file-id", response.dropboxFileId());
    }

    @Test
    @DisplayName("Delete attachment by ID")
    @Sql(scripts = {
            "classpath:database/user/create-user.sql",
            "classpath:database/project/create-project.sql",
            "classpath:database/task/create-task.sql",
            "classpath:database/attachment/create-attachment.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/task/delete-task-with-id-1.sql",
            "classpath:database/project/delete-project-with-id-1.sql",
            "classpath:database/user/delete-user-with-id-1.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteAttachment_responseNoContent() throws Exception {
        mockMvc.perform(delete("/attachments/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
