package team.huoguo.login.entity.forum.Comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import org.apache.tomcat.jni.User;
import org.hibernate.annotations.GenericGenerator;

import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import java.io.Serializable;


/**
 * @author Pistachio
 * @date 2019/11/15 下午10:17
 */
@Data
@Entity(name="comment")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String comment_content;

    @DateTimeFormat(pattern = "yyyy-MM-dd")   //String 反序列化为java.util.date
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private String comment_time;

    @Column(nullable = false)
    private String article_id;  //那一篇文章

    @Column(nullable = false)
    private String from_user_id;



}
