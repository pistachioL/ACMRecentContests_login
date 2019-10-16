package team.huoguo.login.exception;

/**
 * 自定义异常(CustomException)
 * @author GreenHatHG
 **/
public class CustomException extends RuntimeException {

    public CustomException(String msg){
        super(msg);
    }

    public CustomException() {
        super();
    }
}
