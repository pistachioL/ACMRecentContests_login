package team.huoguo.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import team.huoguo.login.bean.rbac.UserInfo;

/**
 * @author GreenHatHG
 */

public interface UserRepository extends JpaRepository<UserInfo, String> {

    UserInfo findByUsername(String username);

    UserInfo findByMail(String mail);
    /**
     * @param username 用户名
     * @return 数据库中对应的密码
     */
    @Query(value = "select password from userinfo where username = ?1", nativeQuery = true)
    String getCredentials(String username);

    @Query(value = "select mail from userinfo where id = ?1")
    String findEmailById(String id);

    @Transactional
    @Modifying
    @Query(value = "update userinfo t set t.username=?1 where t.id=?2", nativeQuery = true)
    void updateUsernameById(String username, String id);

    @Transactional
    @Modifying
    @Query(value = "update userinfo t set t.city=?1 where t.id=?2", nativeQuery = true)
    void updateCityById(String city, String id);

    @Transactional
    @Modifying
    @Query(value = "update userinfo t set t.password=?2 where t.mail=?1", nativeQuery = true)
    void updatePasswordByEmail(String email, String password);

    @Transactional
    @Modifying
    @Query(value = "update userinfo t set t.mail=?1 where t.id=?2", nativeQuery = true)
    void updateEmailById(String email, String id);

    @Transactional
    @Modifying
    @Query(value = "update userinfo t set t.password=?1 where t.id=?2", nativeQuery = true)
    void updatePasswordById(String password, String id);
}