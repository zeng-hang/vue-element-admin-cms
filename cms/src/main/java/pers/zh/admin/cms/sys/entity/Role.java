package pers.zh.admin.cms.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 18:43
 */
@Data
@TableName("sys_role")
public class Role {
    @TableId
    private Integer roleId;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色标识
     */
    private String role;

    /**
     * 角色描述
     */
    private String remark;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();

    /**
     * 菜单列表
     */
    @TableField(exist = false)
    private Set<Long> menuIdList;
}
