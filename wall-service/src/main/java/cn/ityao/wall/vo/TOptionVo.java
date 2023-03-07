package cn.ityao.wall.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * wall-service >>> 【cn.ityao.wall.vo】
 * 配置项vo
 *
 * @author: tongyao
 * @since: 2023-02-21
 */
@Data
public class TOptionVo {
    @NotBlank(message = "文件保存位置不能为空！")
    private String saveFilePath;
    private String beian;

    @NotBlank(message = "令牌过期时间不能为空！")
    private String expireDate;

    @NotBlank(message = "令牌密钥不能为空！")
    private String secret;

    @NotBlank(message = "首页标题不能为空！")
    private String homeTitle;
    private String initTagId;
}
