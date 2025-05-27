package task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import task.dto.UserResponseDto;
import task.dto.UserUpdateRequestDto;
import task.dto.UserUpdateRoleRequestDto;
import task.dto.UserWithRoleDto;
import task.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description =
        "Endpoints for get and update current info about user")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get current user",
            description = "Get info about current user")
    public UserWithRoleDto getUser(Authentication authentication) {
        return userService.getMyUserInfo(authentication);
    }

    @PatchMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update current user",
            description = "Update info about current user")
    public UserResponseDto updateUser(
            Authentication authentication,
            @RequestBody @Valid UserUpdateRequestDto requestDto) {
        return userService.updateUser(authentication, requestDto);
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user`s role",
            description = "Update user`s role by id")
    public UserWithRoleDto updateUserRole(@PathVariable Long id,
                                          @RequestBody @Valid UserUpdateRoleRequestDto requestDto) {
        return userService.updateUserRole(id, requestDto);
    }

}
