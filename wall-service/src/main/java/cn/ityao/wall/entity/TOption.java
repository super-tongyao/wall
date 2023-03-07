package cn.ityao.wall.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 配置表
 * </p>
 *
 * @author tongyao
 * @since 2023-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TOption implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置键
     */
    @TableId(value ="option_key")
    private String optionKey;

    /**
     * 配置内容
     */
    private String optionValue;

    /**
     * 创建者
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String createBy;

    /**
     * 创建时间
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


}
