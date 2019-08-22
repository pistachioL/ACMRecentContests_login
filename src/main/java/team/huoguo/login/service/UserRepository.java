package team.huoguo.login.service;

import org.springframework.data.jpa.repository.JpaRepository;
import team.huoguo.login.bean.UserInfo;

/**
 * @author GreenHatHG
 */

public interface UserRepository extends JpaRepository<UserInfo, String> {

    UserInfo findByUsername(String username);
}
