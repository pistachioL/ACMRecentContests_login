package team.huoguo.login.entity.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


@Component
public interface ForumRepository extends JpaRepository<Forum, String> {
    Forum findAllById(String id);
}

