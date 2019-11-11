package team.huoguo.login.entity.forum;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@ResponseBody

 public class ForumController {
    @Autowired
    ForumRepository forumRepository;  //注入实例

//    @PostMapping("/postComment")


@RequestMapping(value = "/postArticle", method = RequestMethod.POST)
    public Forum postForum(@RequestBody Forum forum){   //发送评论内容
            return forumRepository.save(forum);

    }

    @GetMapping(value = "getTitle")
    public List<Forum> getTitle(){   //获取评论标题
        return forumRepository.findAll();
    }


    @GetMapping(value = "getDetail/{id}")
    public Forum getDetail(@PathVariable String id) {   //获取详情页内容
        return forumRepository.findAllById(id);
    }


}
