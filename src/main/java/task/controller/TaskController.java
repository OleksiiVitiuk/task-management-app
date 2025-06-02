package task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import task.dto.TaskCreateRequestDto;
import task.dto.TaskDto;
import task.service.TaskService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
@Tag(name = "Task", description =
        "Endpoints for get, add, update and delete task")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get tasks",
            description = "Get tasks")
    public Page<TaskDto> getTasks(Pageable pageable) {
        return taskService.getTasks(pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create task",
            description = "Create task and send Email to assignee that he got task")
    public TaskDto createTask(Authentication authentication,
                              @RequestBody @Valid TaskCreateRequestDto createRequestDto) {
        return taskService.createTask(authentication, createRequestDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update task",
            description = "Update task and send Email to assignee about it")
    public TaskDto updateTask(Authentication authentication,
                              @PathVariable Long id,
                              @RequestBody @Valid TaskCreateRequestDto createRequestDto) {
        return taskService.updateTask(authentication, createRequestDto, id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get task",
            description = "Get task by id")
    public TaskDto getTask(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete task",
            description = "Soft delete task")
    public void deleteTask(@PathVariable Long id,
                           Authentication authentication) {
        taskService.deleteTask(id, authentication);
    }
}
