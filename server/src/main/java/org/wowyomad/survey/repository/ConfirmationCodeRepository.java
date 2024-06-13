package org.wowyomad.survey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wowyomad.survey.entity.ConfirmationCode;
import org.wowyomad.survey.entity.ConfirmationType;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {
    Optional<ConfirmationCode> findByEmailAndCodeAndType(String email, String code, ConfirmationType type);
    void deleteByExpirationTimeBefore(LocalDateTime expirationTime);
}
