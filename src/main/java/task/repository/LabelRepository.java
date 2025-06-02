package task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.model.Label;

public interface LabelRepository extends JpaRepository<Label, Long> {
}
