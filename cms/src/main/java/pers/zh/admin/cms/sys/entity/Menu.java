package pers.zh.admin.cms.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 17:46
 */
@Data
@TableName("sys_menu")
public class Menu {
    @TableId
    private Long menuId;

    /**
     * 父级菜单
     */
    private Long parentId;

    /**
     * 路由名称,唯一值
     */
    private String routeName;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 图标
     */
    private String icon;

    /**
     * 权限标识，多个权限用英文逗号分隔
     */
    private String perms;

    /**
     * 类型     0：目录   1：菜单   2：按钮
     */
    private Short type;

    /**
     * 跳转模板
     */
    private String component;

    /**
     * 重定向地址
     */
    private String redirect;

    /**
     * 是否显示
     */
    private Boolean isHidden = true;

    /**
     * 是否不展示子路由
     */
    private Boolean waysShow = false;

    /**
     * 是否显示在顶部导航
     */
    private Boolean isCache = true;

    /**
     * 排序
     */
    private Integer orderNum;

}
