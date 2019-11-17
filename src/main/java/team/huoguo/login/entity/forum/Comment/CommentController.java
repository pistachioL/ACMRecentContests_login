package team.huoguo.login.entity.forum.Comment;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.huoguo.login.entity.forum.Article.Article;
import team.huoguo.login.entity.forum.Article.ArticleRepository;
import team.huoguo.login.shiro.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * @author Pistachio
 * @date 2019/11/16 上午10:16
 */
@RestController
@ResponseBody
@RequestMapping(value="/api/v1")

public class CommentController {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ArticleRepository articleRepository;



    //在某篇文章中发表评论
    //点击回帖时，发送该文章的id到服务器
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Comment postComment(HttpServletRequest request, @RequestBody JSONObject jsonObject){
        Comment comment = new Comment();
        comment.setId(jsonObject.getStr("id"));
        comment.setUser_id(JWTUtil.getId(request.getHeader("Authorization")));  //获取用户id
        comment.setComment_content(jsonObject.getStr("content"));
        comment.setComment_time(jsonObject.getStr("date"));
        comment.setArticle_id(jsonObject.getStr("id")); //对应文章的id
        return commentRepository.save(comment);

    }


    @GetMapping(value = "/comments")
    public List<Comment> getComments(){
        return commentRepository.findAll();
    }

}
