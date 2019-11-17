package team.huoguo.login.entity.forum.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.huoguo.login.entity.userinfo.UserRepository;
import team.huoguo.login.shiro.JWTUtil;


import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@ResponseBody
@RequestMapping(value="/api/v1")
 public class ArticleController {
    @Autowired
    ArticleRepository forumRepository;  //注入实例

    @Autowired
    UserRepository userRepository;
//    @PostMapping("/postComment")


    @RequestMapping(value = "/postArticle", method = RequestMethod.POST)
    public Article postForum(HttpServletRequest request, @RequestBody Article forum){   //发送评论内容
        forum.setUser(JWTUtil.getId(request.getHeader("Authorization")));  //获取token
        return forumRepository.save(forum);

    }

    @GetMapping(value = "getTitle")
    public List<Article> getTitle(){   //获取评论标题
         return forumRepository.findAll();
    }


    @GetMapping(value = "getDetail")
    public Article getDetail(@RequestParam String id) {   //获取详情页内容
        return forumRepository.findById(id).get();
    }


    @GetMapping(value = "getOther") //获取头像 姓名
    public List<Object> getOther(){  //返回的是列表类型 很多篇评论
        return userRepository.getInfo();
    }



}
