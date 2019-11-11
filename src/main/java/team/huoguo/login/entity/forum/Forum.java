package team.huoguo.login.entity.forum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name = "post_forum")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Forum implements Serializable {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    @Column
    private String title;
    @Column
    private String content;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")   //String 反序列化为java.util.date
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    public Forum(){

    }

    public Forum(String title,String content,Date date){
        this.title = title;
        this.content = content;
        this.date = date;
    }


}
