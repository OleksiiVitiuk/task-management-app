package task.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import task.config.MapperConfig;
import task.dto.TaskCreateRequestDto;
import task.dto.TaskDto;
import task.model.Task;

@Mapper(config = MapperConfig.class)
public interface TaskMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "assignee.id", target = "assigneeId")
    TaskDto toDto(Task task);

    @Mapping(target = "project", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    Task toModel(TaskDto taskDto);

    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "project", ignore = true)
    Task toModel(TaskCreateRequestDto taskCreateRequestDto);

    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "project", ignore = true)
    void updateTask(@MappingTarget Task task,
                    TaskCreateRequestDto createRequestDto);
}
