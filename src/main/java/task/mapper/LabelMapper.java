package task.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import task.config.MapperConfig;
import task.dto.LabelCreateRequestDto;
import task.dto.LabelDto;
import task.model.Label;

@Mapper(config = MapperConfig.class)
public interface LabelMapper {

    LabelDto toDto(Label label);

    @Mapping(target = "id", source = "taskId")
    Label toModel(LabelCreateRequestDto createRequestDto);

    @Mapping(target = "id", ignore = true)
    void updateLabel(@MappingTarget Label label,
                     LabelCreateRequestDto createRequestDto);

}
