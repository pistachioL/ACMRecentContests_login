package team.huoguo.login.service;

import team.huoguo.login.bean.Result;
import team.huoguo.login.bean.UserInfo;
import team.huoguo.login.consts.ResultCodeEnum;

/**
 * @author GreenHatHG
 */
public class ResultFactory {
    public static Result buildSuccessResult(UserInfo data) {
        return buidResult(ResultCodeEnum.LOGINSUCESS, "成功", data);
    }

    public static Result buildSuccessResult(String message) {
        return new Result(300,message);
    }


    public static Result buildFailResult(String message) {
        return buidResult(ResultCodeEnum.FAIL, message, null);
    }

    public static Result buildUnauthorizedResult(String message) {
        return buidResult(ResultCodeEnum.UNAUTHORIZED, message, null);
    }

    public static Result buidResult(ResultCodeEnum resultCode, String message, UserInfo data) {
        return buidResult(resultCode.code, message, data);
    }

    public static Result buidResult(int resultCode, String message, UserInfo data) {
        return new Result(resultCode, message, data);
    }
}
