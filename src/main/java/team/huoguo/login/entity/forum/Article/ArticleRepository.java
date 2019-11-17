package team.huoguo.login.entity.forum.Article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


import java.util.Optional;

@Component
public interface ArticleRepository extends JpaRepository<Article, String> {

}

