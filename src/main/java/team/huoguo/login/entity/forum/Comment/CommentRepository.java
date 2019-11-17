package team.huoguo.login.entity.forum.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestBody;
import team.huoguo.login.entity.forum.Article.Article;

import java.util.List;

/**
 * @author Pistachio
 * @date 2019/11/16 上午10:01
 */

public interface CommentRepository extends JpaRepository<Comment,String> {
        @Query(value = "select c.comment_content from comment c INNER JOIN article a ON a.id = c.article_id",nativeQuery = true)
        List<Object> findCommentById();


}



