package GDG4th.personaChat.auth.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmailSessionRepository extends CrudRepository<EmailSession, String> {
    Optional<EmailSession> findByEmail(String email);
}
