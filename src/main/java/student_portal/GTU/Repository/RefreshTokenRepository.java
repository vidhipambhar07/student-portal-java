package student_portal.GTU.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import student_portal.GTU.Model.RefreshToken;
import student_portal.GTU.Model.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
    void deleteByUser(User user);
}
