package team.huoguo.login.entity.forum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import team.huoguo.login.entity.userinfo.UserInfo;

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

    /**
     *  1. 映射单向 n-1 的关联关系
     *  2. 使用 @ManyToOne 来映射多对一的关联关系
     *  3. 使用 @JoinColumn 来映射外键
     *  4. 使用 @ManyToOne 的 fetch 属性来修改默认的关联属性加载策略
     *  5. @JoinColumn的name映射外键的名称。如果不使用主键作为外键，则需要设置referencedColumnName属性
     *  6.   可选属性optional=false,表示user不能为空。删除文章，不影响用户
     */
//  //  @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)//可选属性optional=false,表示author不能为空。删除文章，不影响用户
//    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.ALL})
//    @JoinColumn(name="user_id",referencedColumnName = "id")
//    private UserInfo user;//所属作者
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")   //String 反序列化为java.util.date
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String user;



}
