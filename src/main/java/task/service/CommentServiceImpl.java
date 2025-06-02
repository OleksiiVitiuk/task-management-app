package task.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import task.dto.CommentCreateRequestDto;
import task.dto.CommentDto;
import task.exception.EntityNotFoundException;
import task.mapper.CommentMapper;
import task.model.Comment;
import task.model.Task;
import task.model.User;
import task.repository.CommentRepository;
import task.repository.TaskRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto createComment(Authentication authentication,
                                    CommentCreateRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();

        Task task = taskRepository.findById(requestDto.getTaskId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "There is no task with id: " + requestDto.getTaskId()));

        Comment comment = new Comment();
        comment.setUser(user);
        task.addComment(comment);
        comment.setText(requestDto.getText());
        comment.setTimestamp(LocalDateTime.now());

        taskRepository.flush();
        return commentMapper.toDto(comment);
    }

    @Override
    public Page<CommentDto> getCommentsByTaskId(Pageable pageable, Long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "There is no task with id: " + id);
        }
        return commentRepository.findByTaskId(pageable, id).map(commentMapper::toDto);
    }

}
