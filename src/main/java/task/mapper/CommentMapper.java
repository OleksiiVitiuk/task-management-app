package task.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import task.config.MapperConfig;
import task.dto.CommentDto;
import task.model.Comment;

@Mapper(config = MapperConfig.class)
public interface CommentMapper {

    @Mapping(target = "taskId", source = "task.id")
    @Mapping(target = "userId", source = "user.id")
    CommentDto toDto(Comment comment);
}
