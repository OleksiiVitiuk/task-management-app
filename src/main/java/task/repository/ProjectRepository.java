package task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
