package team.huoguo.login.entity.userinfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OrderBy;
import java.util.List;

/**
 * @author GreenHatHG
 */

public interface UserRepository extends JpaRepository<UserInfo, String> {

    UserInfo findByUsername(String username);

    UserInfo findByMail(String mail);
    /**
     * @param id
     * @return 数据库中对应的密码
     */
    @Query(value = "select password from userinfo where id = ?1", nativeQuery = true)
    String getCredentials(String id);

    @Query(value = "select mail from userinfo where id = ?1",nativeQuery = true)
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

    @Transactional
    @Modifying
    @Query(value = "update userinfo t set t.avatar=?1 where t.id=?2", nativeQuery = true)
    void updateAvatarById(String avatar, String id);


    @Query(value = "select distinct f.id, u.avatar, u.username, f.title,f.date,f.content from userinfo u, article f where f.user=u.id;",nativeQuery = true)
    List<Object> getInfo();





}
