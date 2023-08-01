package cn.ityao.wall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 标签表
 * </p>
 *
 * @author tongyao
 * @since 2023-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签编号
     */
    @TableId(value ="tag_id", type = IdType.UUID)
    private String tagId;

    /**
     * 标签名称
     */
    @NotBlank(message = "标签名称不能为空！")
    private String tagName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建者
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改者
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String modifyBy;

    /**
     * 修改时间
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;

    /** 密码 */
    private String tagPassword;

    /** 是否密码 */
    private String tagPasswordFlag;

    public void setTagPasswordBoolean(Boolean tagPasswordFlag) {
        this.tagPasswordFlag = String.valueOf(tagPasswordFlag == true ? 1 : 0);
    }
}
