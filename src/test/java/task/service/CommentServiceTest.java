package task.service;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import task.dto.CommentCreateRequestDto;
import task.dto.CommentDto;
import task.exception.EntityNotFoundException;
import task.model.User;
import task.util.TestUtil;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    private String jwtToken;

    @BeforeEach
    void setUpAuth() {
        User user = TestUtil.getUser();

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }


    @Test
    @DisplayName("Create comment - success")
    @Sql(scripts = {"classpath:database/user/create-user.sql",
            "classpath:database/project/create-project.sql",
            "classpath:database/task/create-task.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/task/delete-task-with-id-1.sql",
            "classpath:database/comment/delete-comment-with-id-1.sql",
            "classpath:database/project/delete-project-with-id-1.sql",
            "classpath:database/user/delete-user-with-id-1.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCommentTest_createTest_ok() {
        // Given
        CommentCreateRequestDto dto = new CommentCreateRequestDto();
        dto.setTaskId(1L);
        dto.setText("Integration comment");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // When
        CommentDto response = commentService.createComment(auth, dto);

        // Then
        assertEquals("Integration comment", response.text());
        assertEquals(1L, response.taskId());
    }

    @Test
    @DisplayName("Get comments by task ID - success")
    @Sql(scripts = {
            "classpath:database/user/create-user.sql",
            "classpath:database/project/create-project.sql",
            "classpath:database/task/create-task.sql",
            "classpath:database/comment/create-comment.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/comment/delete-comment-with-id-1.sql",
            "classpath:database/task/delete-task-with-id-1.sql",
            "classpath:database/project/delete-project-with-id-1.sql",
            "classpath:database/user/delete-user-with-id-1.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCommentsByTaskIdTest_getCommentsByTaskId_ok() {
        Pageable pageable = PageRequest.of(0, 10);
        Long taskId = 1L;

        Page<CommentDto> result = commentService.getCommentsByTaskId(pageable, taskId);

        assertEquals(1, result.getTotalElements());
        assertEquals("text", result.getContent().get(0).text());
        assertEquals(1L, result.getContent().get(0).taskId());
        assertEquals(1L, result.getContent().get(0).userId());
    }

    @Test
    @DisplayName("Get comments by task ID - task not found")
    void getCommentsByTaskIdTest_getCommentsByTaskId_notOk() {
        Pageable pageable = PageRequest.of(0, 10);
        Long nonexistentTaskId = 999L;

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                commentService.getCommentsByTaskId(pageable, nonexistentTaskId));

        assertEquals("There is no task with id: 999", ex.getMessage());
    }
}
