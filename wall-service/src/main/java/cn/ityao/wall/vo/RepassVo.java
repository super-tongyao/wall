package cn.ityao.wall.vo;

import lombok.Data;

/**
 * wall-service >>> 【cn.ityao.wall.vo】
 * 修改密码vo
 *
 * @author: tongyao
 * @since: 2023-02-14
 */
@Data
public class RepassVo {
    private String oldPass;
    private String newPass;
    private String reNewPass;
}
