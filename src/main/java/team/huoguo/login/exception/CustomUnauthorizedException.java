package team.huoguo.login.exception;

/**
 * 自定义401无权限异常(UnauthorizedException)
 * @author GreenHatHG
 **/
public class CustomUnauthorizedException extends RuntimeException {

    public CustomUnauthorizedException(String msg){
        super(msg);
    }

    public CustomUnauthorizedException() {
        super();
    }
}
