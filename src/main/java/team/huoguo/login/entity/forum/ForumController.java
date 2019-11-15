package team.huoguo.login.entity.forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.huoguo.login.entity.resp.Result;
import team.huoguo.login.entity.userinfo.UserInfo;
import team.huoguo.login.entity.userinfo.UserRepository;
import team.huoguo.login.shiro.JWTUtil;


import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@ResponseBody
@RequestMapping(value="/api/v1")
 public class ForumController {
    @Autowired
    ForumRepository forumRepository;  //注入实例

    @Autowired
    UserRepository userRepository;
//    @PostMapping("/postComment")


@RequestMapping(value = "/postArticle", method = RequestMethod.POST)
    public Forum postForum(HttpServletRequest request, @RequestBody Forum forum){   //发送评论内容
        forum.setUser(JWTUtil.getId(request.getHeader("Authorization")));  //获取token
        return forumRepository.save(forum);

    }

    @GetMapping(value = "getTitle")
    public List<Forum> getTitle(){   //获取评论标题
        return forumRepository.findAll();
    }


    @GetMapping(value = "getDetail")
    public Forum getDetail(@RequestParam String id) {   //获取详情页内容
        return forumRepository.findAllById(id);
    }


    @GetMapping(value = "getOther") //获取头像 姓名
    public List<Object> getOther(){  //返回的是列表类型 很多篇评论
        return userRepository.getInfo();
    }



}
