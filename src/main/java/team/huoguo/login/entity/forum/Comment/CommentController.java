package team.huoguo.login.entity.forum.Comment;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.huoguo.login.entity.forum.Article.Article;
import team.huoguo.login.entity.forum.Article.ArticleRepository;
import team.huoguo.login.entity.userinfo.UserInfo;
import team.huoguo.login.entity.userinfo.UserRepository;
import team.huoguo.login.shiro.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private UserRepository userRepository;


    //在某篇文章中发表评论
    //点击回帖时，发送该文章的id到服务器
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Comment postComment(HttpServletRequest request, @RequestBody JSONObject jsonObject){
        Comment comment = new Comment();
        comment.setId(jsonObject.getStr("id"));  //该评论的id
        comment.setFrom_user_id(JWTUtil.getId(request.getHeader("Authorization")));  //获取用户id
        comment.setComment_content(jsonObject.getStr("content"));
        comment.setComment_time(jsonObject.getDate("date"));
        comment.setArticle_id(jsonObject.getStr("id")); //对应文章的id
        return commentRepository.save(comment);

    }


    @GetMapping(value = "/comments")
    public JSONObject getComments(@RequestParam String id){   //查看评论
        JSONObject jsonObject = new JSONObject();

        Article article  = (Article)articleRepository.findCommentListById(id); //通过id找到对应的文章,并查找出文章的所有CommentList
        List <Comment> comments = article.getCommentList();

//        jsonObject.put("comments", comments);  //把数据库中comments的内容放进jsonObject
//        处理评论的用户
        List<Map<String, String>> info = new ArrayList<>();
        for(Comment comment :comments){
            Map<String, String> map = new HashMap<>(2);
            Optional<UserInfo> optionalUserInfo = userRepository.findById(comment.getFrom_user_id()); //从评论表中取到相应的用户信息
            if(!optionalUserInfo.isPresent()){
                continue;
            }
            UserInfo userInfo = optionalUserInfo.get();
            map.put("username", userInfo.getUsername());
            map.put("avatar", userInfo.getAvatar());
            map.put("comment_content",comment.getComment_content());
            SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd" );
            String strdate = sdf.format(comment.getComment_time());
            map.put("comment_time",strdate);
            info.add(map);
        }

        jsonObject.put("commentList",info);
        return jsonObject;
    }

    @GetMapping(value="/counts")
    public int getCounts(@RequestParam String id){
        Article article  = (Article)articleRepository.findCommentListById(id); //通过id找到对应的文章,并查找出文章的所有CommentList
        return article.getCommentList().size();

    }

}
