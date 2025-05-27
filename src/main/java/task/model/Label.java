package task.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "labels")
@Getter
@Setter
@SQLDelete(sql = "UPDATE labels SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Label {

    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Color color;
    @MapsId
    @OneToOne
    @JoinColumn(name = "task_id")
    private Task task;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDeleted = false;

    public enum Color {
        RED,
        GREEN,
        BLUE
    }
}
