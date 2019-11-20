package team.huoguo.login.entity.forum.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestBody;
import team.huoguo.login.entity.forum.Article.Article;

import java.util.List;

/**
 * @author Pistachio
 * @date 2019/11/16 上午10:01
 */

public interface CommentRepository extends JpaRepository<Comment,String> {
//        @Query(value = "select distinct c.comment_content,u.username,u.avatar,c.comment_time from comment c,article a,userinfo u where a.id = c.article_id AND a.user_id=u.id;",nativeQuery = true)
//        List<Object> findCommentById();
     //   int countByArticle_id(String id);
     //   List<Object> findUserInfoById(String id);

}



