package team.huoguo.login.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author GreenHatHG
 **/

@Data
@AllArgsConstructor
public class ErrorResponseEntity {

    private int code;
    private String message;

}
