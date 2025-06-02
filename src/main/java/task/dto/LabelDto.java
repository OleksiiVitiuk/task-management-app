package task.dto;

import lombok.Data;
import task.model.Label;

@Data
public class LabelDto {
    private Long id;
    private String name;
    private Label.Color color;
}
