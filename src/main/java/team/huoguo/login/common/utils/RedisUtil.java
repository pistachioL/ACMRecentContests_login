package team.huoguo.login.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author GreenHatHG
 **/

@Configuration
public class RedisUtil {

    /**
     * 三分钟内可发送邮件次数
     */
    @Value("${TIMES}")
    private int times;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 过期时间，三分钟
     */
    private final static int EXPIRE_TIME = 3*60;
    /**
     * 指定缓存失效时间
     * @param key  key值
     * @param time 缓存时间
     */
    public void expire(String key, long time) {
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 普通缓存放入String类型的value
     *
     * @param key   键值
     * @param value 值
     */
    public void setString(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        //十分钟过期
        expire(key, EXPIRE_TIME);
    }

    /**
     * 普通缓存放入int类型的value
     * @param key
     * @param value
     */
    public void setInt(String key, long value){
        redisTemplate.opsForValue().increment(key, value);
        expire(key, EXPIRE_TIME);
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object getString(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除对应的key
     * @param key
     */
    public void deleteKey(String key){
        redisTemplate.delete(key);
    }

    /**
     * 查询发送给某个邮箱的次数是否过多
     * @param mail
     * @return
     */
    public boolean tooManyTimes(String mail){
        mail = mail + "times";
        Object o = getString(mail);
        if(o == null){
            setInt(mail, 1);
            return false;
        }
        int num = Integer.parseInt(o.toString());
        if(num > times){
            return true;
        }else{
            num++;
            setInt(mail, num);
            return false;
        }
    }

}
