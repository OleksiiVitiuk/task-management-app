package task.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;
import task.model.Role;

@Data
public class UserUpdateRoleRequestDto {
    @NotNull
    private Set<Role.RoleName> roles;
}
