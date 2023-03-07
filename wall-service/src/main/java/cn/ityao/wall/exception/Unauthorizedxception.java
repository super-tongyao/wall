package cn.ityao.wall.exception;

import lombok.Data;

/**
 * byte-cabinet >>> 【com.cabinet.common.exception】
 * 自定义权限异常
 *
 * @author: tongyao
 * @since: 2023-02-14
 */
@Data
public class Unauthorizedxception extends RuntimeException {
    Integer code;
    public Unauthorizedxception(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
