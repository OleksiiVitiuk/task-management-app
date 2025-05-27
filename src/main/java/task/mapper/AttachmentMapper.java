package task.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import task.config.MapperConfig;
import task.dto.AttachmentDto;
import task.model.Attachment;

@Mapper(config = MapperConfig.class)
public interface AttachmentMapper {

    @Mapping(target = "taskId", source = "task.id")
    AttachmentDto toDto(Attachment attachment);
}
