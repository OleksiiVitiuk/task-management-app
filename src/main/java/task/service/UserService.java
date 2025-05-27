package task.service;

import org.springframework.security.core.Authentication;
import task.dto.UserRegistrationRequestDto;
import task.dto.UserResponseDto;
import task.dto.UserUpdateRequestDto;
import task.dto.UserUpdateRoleRequestDto;
import task.dto.UserWithRoleDto;
import task.exception.RegistrationException;

public interface UserService {

    public UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException;

    UserWithRoleDto getMyUserInfo(Authentication authentication);

    UserResponseDto updateUser(
            Authentication authentication,
            UserUpdateRequestDto requestDto);

    UserWithRoleDto updateUserRole(Long id, UserUpdateRoleRequestDto requestDto);
}
