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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import task.dto.CommentCreateRequestDto;
import task.dto.CommentDto;
import task.service.CommentService;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "Comment", description =
        "Endpoints for add and read task`s comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add comment",
            description = "Add comment to task")
    public CommentDto addComment(Authentication authentication,
                                 @RequestBody @Valid CommentCreateRequestDto requestDto) {
        return commentService.createComment(authentication, requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get comments",
            description = "Get task`s comments")
    public Page<CommentDto> getCommentsByTaskId(Pageable pageable, Long taskId) {
        return commentService.getCommentsByTaskId(pageable, taskId);
    }

}
