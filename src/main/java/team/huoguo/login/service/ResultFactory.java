package team.huoguo.login.service;

import org.springframework.stereotype.Component;
import team.huoguo.login.bean.Result;
import team.huoguo.login.consts.ResultCodeEnum;

/**
 * @author GreenHatHG
 */

@Component
public class ResultFactory {
    public static Result buildSuccessResult(Object data) {
        return new Result(ResultCodeEnum.SUCCESS.code, "成功", data);
    }

    public static Result buildSuccessResult(String message) {
        return new Result(ResultCodeEnum.SUCCESS.code, message);
    }

    public static Result buildFailResult(String message) {
        return new Result(ResultCodeEnum.FAIL.code, message);
    }

    public static Result buildUnauthorizedResult(String message) {
        return new Result(ResultCodeEnum.UNAUTHORIZED.code, message);
    }
}
