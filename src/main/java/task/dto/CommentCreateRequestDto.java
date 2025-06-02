package task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CommentCreateRequestDto {
    @NotNull
    @Positive
    private Long taskId;
    @NotBlank
    private String text;
}
