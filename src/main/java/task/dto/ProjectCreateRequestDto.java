package task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;
import task.model.Status;

@Data
public class ProjectCreateRequestDto {
    @NotBlank
    @Size(min = 4, message = "Name should be longer than 4")
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private Status status;
}
