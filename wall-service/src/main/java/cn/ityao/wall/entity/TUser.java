package cn.ityao.wall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 *  用户表
 * </p>
 *
 * @author tongyao
 * @since 2023-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @TableId(value ="user_id", type = IdType.UUID)
    private String userId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户密码
     */
    private String passWord;

    /**
     * 性别（1为男，2为女）
     */
    private Integer sex;

    /**
     * 电话手机
     */
    private String phone;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 最后登录的时间
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastTime;

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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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

    /**
     * access_token
     */
    @TableField(exist = false)
    private String access_token;
}
