package team.huoguo.login.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    @Modifying
    @Query(value = "update userinfo t set t.status=?2 where t.username=?1", nativeQuery = true)
    void updateStatusByUsername(String username, int status);

    UserInfo findByMail(String mail);

    @Transactional
    @Modifying
    @Query(value = "update userinfo t set t.password=?2 where t.mail=?1", nativeQuery = true)
    void updatePasswordByEmail(String email, String password);
}
