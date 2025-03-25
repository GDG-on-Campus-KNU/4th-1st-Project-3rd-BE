package GDG4th.personaChat.auth.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends CrudRepository<EmailVerification, String> {
    Optional<EmailVerification> findByEmail(String email);
}
