package task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import task.dto.ProjectCreateRequestDto;
import task.dto.ProjectDto;
import task.service.ProjectService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
@Tag(name = "Project", description =
        "Endpoints for get, add, update and delete project")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get projects",
            description = "Get projects")
    public Page<ProjectDto> getProjects(Pageable pageable) {
        return projectService.getProjects(pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create project",
            description = "Create project")
    public ProjectDto createProject(
            @RequestBody @Valid ProjectCreateRequestDto createRequestDto) {
        return projectService.createProject(createRequestDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update project",
            description = "Update project")
    public ProjectDto updateProject(
            @PathVariable Long id,
            @RequestBody @Valid ProjectCreateRequestDto createRequestDto) {
        return projectService.updateProject(id, createRequestDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get project",
            description = "Get project by id")
    public ProjectDto getProject(
            @PathVariable Long id) {
        return projectService.getProject(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete project",
            description = "Soft delete project")
    public void deleteProject(
            @PathVariable Long id) {
        projectService.deleteProject(id);
    }

}
