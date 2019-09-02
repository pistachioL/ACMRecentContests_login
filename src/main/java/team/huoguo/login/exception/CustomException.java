package team.huoguo.login.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常处理
 * @author GreenHatHG
 */

@Getter
@Setter
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 4564124491192825748L;

    private int code;

    public CustomException(int code, String message) {
        super(message);
        this.setCode(code);
    }
}