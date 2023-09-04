package cn.ityao.wall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 资源表
 * </p>
 *
 * @author tongyao
 * @since 2023-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资源编号
     */
    @TableId(value ="resource_id", type = IdType.UUID)
    private String resourceId;

    /**
     * 标题标题描述
     */
    @Length(min = 0, max = 300, message = "标题长度不能超过500个字符！")
    private String title;

    /**
     * 封面路径
     */
    private String coverPath;

    /**
     * 资源物理存储路径
     */
    private String resourcePath;

    /**
     * 标签编号（可多个，以英文逗号隔开）
     */
    private String tagId;

    /**
     * 资源来源
     */
    private String sourceType;

    /**
     * 资源格式
     */
    private String resourceFormat;

    /**
     * 可见状态（1、为可见 0、为不可见）
     */
    private boolean visibleFlag;

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

    @TableField(exist = false)
    private String bedUrl;
}
