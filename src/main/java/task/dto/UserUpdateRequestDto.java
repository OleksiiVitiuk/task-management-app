package task.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import task.validation.FieldMatches;

@Data
@FieldMatches(fields = {"password", "repeatPassword"})
public class UserUpdateRequestDto {
    @Email
    private String email;
    @Size(min = 3, message = "username should be longer than 3")
    private String username;
    @Size(min = 8, message = "Password should be longer than 8")
    private String password;
    @Size(min = 8, message = "Password should be longer than 8")
    private String repeatPassword;
    @Size(max = 25, message = "FirstName should be longer than 25")
    private String firstName;
    @Size(max = 25, message = "LastName should be longer than 25")
    private String lastName;
}
