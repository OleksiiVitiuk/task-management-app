package task.mapper;

import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import task.config.MapperConfig;
import task.dto.UserRegistrationRequestDto;
import task.dto.UserResponseDto;
import task.dto.UserUpdateRequestDto;
import task.dto.UserWithRoleDto;
import task.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto userRegistrationRequestDto);

    @Mapping(target = "roleNames", ignore = true)
    UserWithRoleDto toDtoWithRole(User user);

    @AfterMapping
    default void userRolesToString(@MappingTarget UserWithRoleDto userWithRoleDto,
                                   User user) {
        userWithRoleDto.setRoleNames(user.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet()));
    }

    void updateUser(@MappingTarget User user, UserUpdateRequestDto requestDto);
}
