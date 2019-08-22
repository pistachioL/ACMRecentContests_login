package team.huoguo.login.bean;

import lombok.Data;

/**
 * @author GreenHatHG
 */

@Data
public class Result {

    //响应状态码
    private int code;

    //响应提示信息
    private String message;

    //响应结果对象
    private UserInfo data;

    public Result(int code, String message, UserInfo loginInfo) {
        this.code = code;
        this.message = message;
        this.data = loginInfo;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
