package pers.zh.admin.cms.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 18:48
 */
@Data
@TableName("sys_user_role")
public class UserRole {
    @TableId
    private Long userRoleId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Integer roleId;
}
