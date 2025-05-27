package task.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "projects")
@Getter
@Setter
@SQLDelete(sql = "UPDATE projects SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private LocalDate startDate;
    private LocalDate endDate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDeleted = false;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "project",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE})
    private List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        task.setProject(this);
        tasks.add(task);
    }
}
