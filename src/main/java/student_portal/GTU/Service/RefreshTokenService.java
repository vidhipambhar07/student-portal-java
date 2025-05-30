package student_portal.GTU.Service;

import org.springframework.stereotype.Repository;
import student_portal.GTU.Model.RefreshToken;
import student_portal.GTU.Model.User;

import java.util.Optional;
@Repository

public interface RefreshTokenService {
    RefreshToken createOrUpdateRefreshToken(User user);
    void deleteByUser(User user);
    boolean isTokenExpired(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);

    RefreshToken verifyExpiration(RefreshToken token);

}
