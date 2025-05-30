package student_portal.GTU.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import student_portal.GTU.Model.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
