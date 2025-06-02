package task.dto;

import java.util.Set;
import lombok.Data;

@Data
public class UserWithRoleDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> roleNames;

}
