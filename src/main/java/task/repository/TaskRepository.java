package task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
