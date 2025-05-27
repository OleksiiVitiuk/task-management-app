package task.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import task.validation.FieldMatches;

@Data
@FieldMatches(fields = {"password", "repeatPassword"})
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 3, message = "username should be longer than 3")
    private String username;
    @NotBlank
    @Size(min = 8, message = "Password should be longer than 8")
    private String password;
    @NotBlank
    @Size(min = 8, message = "Password should be longer than 8")
    private String repeatPassword;
    @NotBlank
    @Size(max = 25, message = "FirstName should be longer than 25")
    private String firstName;
    @NotBlank
    @Size(max = 25, message = "LastName should be longer than 25")
    private String lastName;

}
