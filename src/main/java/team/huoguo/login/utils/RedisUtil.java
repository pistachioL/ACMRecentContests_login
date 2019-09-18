package team.huoguo.login.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author GreenHatHG
 **/

@Configuration
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

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
        expire(key, 10*60);
    }

    /**
     * 普通缓存放入int类型的value
     * @param key
     * @param value
     */
    public void setInt(String key, long value){
        redisTemplate.opsForValue().increment(key, value);
        expire(key, 10*60);
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
     * 查询发送给某个邮箱的次数是否过多
     * @param mail
     * @return
     */
    public boolean tooManyTimes(String mail){
        if(mail== null){
            return false;
        }else{
            Object o = getString(mail);
            if(o == null){
                setInt(mail, 1);
                return false;
            }
            int num = Integer.parseInt(o.toString());
            if(num > 3){
                return true;
            }else{
                num++;
                setInt(mail, num);
                return false;
            }
        }
    }


}