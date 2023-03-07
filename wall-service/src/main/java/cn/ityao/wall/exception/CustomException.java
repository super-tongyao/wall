package cn.ityao.wall.exception;

import lombok.Data;

/**
 * byte-cabinet >>> 【com.cabinet.common.exception】
 * 自定义异常
 *
 * @author: tongyao
 * @since: 2023-02-14
 */
@Data
public class CustomException extends RuntimeException {
    Integer httpCode;
    Integer JsonCode;
    public CustomException(Integer httpCode, Integer JsonCode, String message) {
        super(message);
        this.httpCode = httpCode;
        this.JsonCode = JsonCode;
    }
}
