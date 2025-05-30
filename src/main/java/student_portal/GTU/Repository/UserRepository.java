package student_portal.GTU.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import student_portal.GTU.Enum.Role;
import student_portal.GTU.Model.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    long countByRole(Role role);
    Optional<User> findByEnrollmentNumber(String enrollmentNumber);  // ✅ Matches field name
    Optional<User> findByResetToken(String token);
    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Optional<Long> findIdByEmail(@Param("email") String email);
    // Or if you have only one role per user:
    @Query("SELECT u.role FROM User u WHERE u.email = :email")
    String findRoleByEmail(@Param("email") String email);

}
