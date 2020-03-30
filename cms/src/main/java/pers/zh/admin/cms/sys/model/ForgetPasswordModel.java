package pers.zh.admin.cms.sys.model;

import lombok.Data;

/**
 * 忘记密码模型
 * <p>
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 20:14
 */
@Data
public class ForgetPasswordModel {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 新密码
     */
    private String password;

    /**
     * 验证方式
     * 1.手机号验证
     * 2.邮箱验证
     */
    private Short type;
}
