package task.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import task.model.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    @Query("SELECT a FROM Attachment a JOIN FETCH a.task t WHERE a.task.id = :taskId")
    Page<Attachment> findByTaskId(Pageable pageable, Long taskId);

    @Query("SELECT a FROM Attachment a WHERE id = :id")
    Optional<Attachment> findById(Long id);

}
