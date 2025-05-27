package task.util;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import org.h2.engine.Role;
import task.dto.ProjectCreateRequestDto;
import task.dto.ProjectDto;
import task.dto.TaskCreateRequestDto;
import task.dto.TaskDto;
import task.dto.UserUpdateRequestDto;
import task.model.Project;
import task.model.Status;
import task.model.Task;
import task.model.User;

public class TestUtil {

    public static Project getProject() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStartDate(LocalDate.of(2025, 5, 27));
        project.setEndDate(LocalDate.of(2025, 12, 31));
        project.setStatus(Status.IN_PROGRESS);
        project.setDeleted(false);
        return project;
    }

    public static ProjectCreateRequestDto getProjectCreateRequestDto() {
        ProjectCreateRequestDto dto = new ProjectCreateRequestDto();
        dto.setName("Test Project");
        dto.setDescription("Test Description");
        dto.setStartDate(LocalDate.of(2025, 5, 27));
        dto.setEndDate(LocalDate.of(2025, 12, 31));
        dto.setStatus(Status.IN_PROGRESS);
        return dto;
    }

    public static ProjectDto getProjectDto() {
        return new ProjectDto(
                1L,
                "Test Project",
                "Test Description",
                LocalDate.of(2025, 5, 27),
                LocalDate.of(2025, 12, 31),
                Status.IN_PROGRESS,
                List.of()
        );
    }

    public static TaskDto getTaskDto() {
        return new TaskDto(1L,
                "task",
                "task desc",
                "LOW",
                "IN_PROGRESS",
                LocalDate.of(2025, 5, 27),
                1L, 1L);
    }

    public static Task getTask() {
        Task task = new Task();
        task.setName("Task");
        task.setDescription("description");
        task.setPriority(Task.Priority.HIGH);
        task.setStatus(Status.IN_PROGRESS);
        task.setDueDate(LocalDate.of(2025, 6, 1));
        task.setProject(getProject());
        task.setAssignee(getUser());
        return task;
    }

    public static User getUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("testPassword123");
        user.setEmail("user@gmail.com");
        user.setRoles(new HashSet<>(Role.USER));
        user.setFirstName("user");
        user.setLastName("user");
        return user;
    }

    public static TaskCreateRequestDto getTaskCreateRequestDto() {
        TaskCreateRequestDto requestDto = new TaskCreateRequestDto();
        requestDto.setName("New Task");
        requestDto.setDescription("New description");
        requestDto.setPriority(Task.Priority.HIGH);
        requestDto.setStatus(Status.IN_PROGRESS);
        requestDto.setDueDate(LocalDate.of(2025, 5, 27));
        requestDto.setProjectId(1L);
        requestDto.setAssigneeId(1L);
        return requestDto;
    }

    public static UserUpdateRequestDto getUserUpdateRequestDto() {
        UserUpdateRequestDto requestDto = new UserUpdateRequestDto();
        requestDto.setEmail("updated@email.com");
        requestDto.setUsername("newuser");
        requestDto.setPassword("newtestPassword123");
        requestDto.setRepeatPassword("newtestPassword123");
        requestDto.setFirstName("Updated");
        requestDto.setLastName("Name");
        return requestDto;
    }

    public static TaskDto getCreatedTask() {
        return new TaskDto(
                1L,
                "New Task",
                "New description",
                Task.Priority.HIGH.toString(),
                Status.IN_PROGRESS.toString(),
                LocalDate.of(2025, 5, 27),
                1L,
                1L
        );
    }
}
