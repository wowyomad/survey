package org.wowyomad.survey.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wowyomad.survey.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByEmailConfirmedFalseAndCreatedAtBefore(LocalDateTime dateTime);
}
