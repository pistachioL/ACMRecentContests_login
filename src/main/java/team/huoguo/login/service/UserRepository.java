package team.huoguo.login.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team.huoguo.login.bean.UserInfo;

/**
 * @author GreenHatHG
 */

public interface UserRepository extends JpaRepository<UserInfo, String> {

    UserInfo findByUsername(String username);

    /**
     * @param username 用户名
     * @return 数据库中对应的密码
     */
    @Query(value = "select password from userinfo where username = ?1", nativeQuery = true)
    String getCredentials(String username);
}
