package pers.zh.admin.cms.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 15:07
 */
@Data
@TableName("sys_user")
public class User {
    /**
     * 主键ID自增长
     */
    @TableId
    private Long userId;

    /**
     * 登录名
     */
    private String userName;

    /**
     * 登录密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * 加密签名值
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String sign;

    /**
     * 用户状态
     */
    private Short status = 1;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别
     */
    private Short gender;

    /**
     * 生日
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 住址
     */
    private String address;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 电话
     */
    private String phone;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();

    /**
     * 用户角色List
     */
    @TableField(exist = false)
    private Set<Integer> rolesList;

    public interface STATUS {
        short NORMAL = 1; //正常
        short LOCK = 0;//锁定
    }

}
