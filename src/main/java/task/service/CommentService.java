package task.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import task.dto.CommentCreateRequestDto;
import task.dto.CommentDto;

public interface CommentService {
    CommentDto createComment(Authentication authentication, CommentCreateRequestDto requestDto);

    Page<CommentDto> getCommentsByTaskId(Pageable pageable, Long taskId);
}
