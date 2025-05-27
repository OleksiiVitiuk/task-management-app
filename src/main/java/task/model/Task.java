package task.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "tasks")
@Getter
@Setter
@SQLDelete(sql = "UPDATE tasks SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(nullable = false)
    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id",
            foreignKey = @ForeignKey(name = "fk_tasks_project"))
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id",
            foreignKey = @ForeignKey(name = "fk_tasks_user"))
    private User assignee;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "task", orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Comment> comments = new ArrayList<>();

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "task", orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();

    @OneToOne(mappedBy = "task",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Label label;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDeleted = false;

    public Task(Long id) {
        this.id = id;
    }

    public void addComment(Comment comment) {
        comment.setTask(this);
        comments.add(comment);
    }

    public void addAttachment(Attachment attachment) {
        attachment.setTask(this);
        attachments.add(attachment);
    }

    public void setLabel(Label label) {
        label.setTask(this);
        this.label = label;
    }

    @Override
    public String toString() {
        return "\nProject: " + project.getName() + "(" + project.getStatus() + ")"
                + "\nTask: " + name + "(" + status + ")"
                + "\nPriority: " + priority
                + "\nDescription: " + description
                + "\nDue to: " + dueDate;
    }

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }
}
