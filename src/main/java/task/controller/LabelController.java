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
import task.dto.LabelCreateRequestDto;
import task.dto.LabelDto;
import task.service.LabelService;

@RestController
@RequestMapping("/labels")
@RequiredArgsConstructor
@Tag(name = "Label", description =
        "Endpoints for get, add, update and delete labels")
public class LabelController {

    private final LabelService labelService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get labels",
            description = "Get labels")
    public Page<LabelDto> getLabels(Pageable pageable) {
        return labelService.getLabels(pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create label",
            description = "Create label for task")
    public LabelDto createLabel(
            @RequestBody @Valid LabelCreateRequestDto createRequestDto) {
        return labelService.createLabels(createRequestDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update label",
            description = "Update label for task")
    public LabelDto updateLabel(
            @PathVariable Long id,
            @RequestBody @Valid LabelCreateRequestDto createRequestDto) {
        return labelService.updateLabel(id, createRequestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete label",
            description = "Soft delete label")
    public void deleteLabel(@PathVariable Long id) {
        labelService.deleteLabel(id);
    }

}
