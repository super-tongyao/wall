package cn.ityao.wall.exception;

import cn.ityao.wall.util.DataResult;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * byte-cabinet >>> 【com.cabinet.auth.exception】
 * 由于@ControllerAdvice捕获不到404，用于只配置404错误，重写error页面
 *
 * @author: tongyao
 * @since: 2023-02-14
 */
@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping(value = "/error")
    public ResponseEntity<DataResult> error(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == 404){
            return ResponseEntity.status(statusCode)
                    .body(DataResult.setResult(statusCode,"接口没有找到"));
        }
        return ResponseEntity.status(500)
                .body(DataResult.setResult(500,"error pages 500"));
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
