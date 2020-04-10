package pers.zh.admin.cms.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 18:50
 */
@Data
@TableName("sys_role_menu")
public class RoleMenu {
    @TableId
    private Long roleMenuId;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 菜单ID
     */
    private Long menuId;
}
