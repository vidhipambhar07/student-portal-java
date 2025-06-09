package student_portal.GTU.ServiceImpl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import student_portal.GTU.Model.RefreshToken;
import student_portal.GTU.Model.User;
import student_portal.GTU.Repository.RefreshTokenRepository;
import student_portal.GTU.Service.RefreshTokenService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.refresh-token.expiry-ms}")
    private Long refreshTokenExpiryMs;

    @Autowired
    public RefreshTokenRepository refreshTokenRepository;


    @Override
    @Transactional
    public RefreshToken createOrUpdateRefreshToken(User user) {
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);

        RefreshToken refreshToken = existingToken.orElseGet(RefreshToken::new);
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(LocalDateTime.ofInstant(Instant.now().plus(7, ChronoUnit.DAYS), ZoneId.systemDefault()));

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    @Override
    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(LocalDateTime.now(ZoneId.systemDefault()));
    }
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now(ZoneId.systemDefault()))) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired. Please login again.");
        }
        return token;
    }

}