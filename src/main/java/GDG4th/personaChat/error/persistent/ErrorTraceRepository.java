package GDG4th.personaChat.error.persistent;

import GDG4th.personaChat.error.domain.ErrorTrace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorTraceRepository extends JpaRepository<ErrorTrace, Long> {
    Page<ErrorTrace> findAll(Pageable pageable);
}
