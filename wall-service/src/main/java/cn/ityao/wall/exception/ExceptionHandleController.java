package cn.ityao.wall.exception;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.ityao.wall.util.DataResult;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一异常处理类
 * 捕获程序所有异常，针对不同异常，采取不同的处理方式
 *
 * @version 2.0
 * @author tongyao
 * @since 2023-02-14
 */
@ControllerAdvice
@RestController
@Log4j2
public class ExceptionHandleController {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<DataResult> handle(Exception e) {
        if(e instanceof CustomException){
            CustomException customException = (CustomException) e;
            return ResponseEntity.status(customException.getHttpCode())
                    .body(DataResult.setResult(customException.getJsonCode(),customException.getMessage()));
        }else if(e instanceof MethodArgumentNotValidException){
            //500 字段为空异常
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            FieldError fieldError = bindingResult.getFieldError();
            return ResponseEntity.status(500)
                    .body(DataResult.setResult(500,fieldError.getDefaultMessage()));
        }else if(e instanceof Unauthorizedxception){
            Unauthorizedxception unauthorizedxception = (Unauthorizedxception) e;
            return ResponseEntity.status(unauthorizedxception.getCode())
                    .body(DataResult.setResult(unauthorizedxception.getCode(),unauthorizedxception.getMessage()));
        }
        log.error(ExceptionUtil.stacktraceToString(e));
        return ResponseEntity.status(500)
                .body(DataResult.setResult(500,e.getMessage()));
    }

}
