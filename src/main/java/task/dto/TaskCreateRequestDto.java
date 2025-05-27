package task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;
import task.model.Status;
import task.model.Task;

@Data
public class TaskCreateRequestDto {
    @NotBlank
    @Size(min = 4, message = "Name should be longer than 4")
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Task.Priority priority;
    @NotNull
    private Status status;
    @NotNull
    private LocalDate dueDate;
    @NotNull
    @Positive
    private Long projectId;
    @NotNull
    @Positive
    private Long assigneeId;
}
