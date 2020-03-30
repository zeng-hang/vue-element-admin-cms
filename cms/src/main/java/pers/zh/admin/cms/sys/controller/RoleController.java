package pers.zh.admin.cms.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import pers.zh.admin.cms.common.utlis.Query;
import pers.zh.admin.cms.common.utlis.R;
import pers.zh.admin.cms.sys.entity.Role;
import pers.zh.admin.cms.sys.entity.RoleMenu;
import pers.zh.admin.cms.sys.service.RoleMenuService;
import pers.zh.admin.cms.sys.service.RoleService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 18:47
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:role:list")
    public R list(@RequestParam Map<String, Object> params) {
        IPage<Role> page = roleService.page(
                new Query<Role>().getPage(params),
                new QueryWrapper<Role>()
                        .like(StringUtils.isNotBlank(String.valueOf(params.get("name"))), "name", params.get("name"))
        );
        return R.success().put("data", page);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:role:info")
    public R info(@PathVariable("id") Long id) {
        Role role = roleService.getById(id);

        Set<Long> menuLists = new HashSet<>();
        roleMenuService.list(
                new QueryWrapper<RoleMenu>()
                        .eq("role_id", role.getRoleId())
        ).forEach(roleMenu -> menuLists.add(roleMenu.getMenuId()));
        role.setMenuIdList(menuLists);

        return R.success().put("data", role);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:role:save")
    public R save(@RequestBody Role role) {
        roleService.save(role);

        //保存角色和菜单的对应关系
        Set<Long> menuList = role.getMenuIdList();
        if (menuList != null && menuList.size() > 0) {
            List<RoleMenu> roleMenus = new ArrayList<>();
            for (Long menu : menuList) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(role.getRoleId());
                roleMenu.setMenuId(menu);
                roleMenus.add(roleMenu);
            }

            roleMenuService.saveBatch(roleMenus);
        }

        return R.success("保存成功");
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:role:update")
    public R update(@RequestBody Role role) {
        roleService.updateById(role);

        //删除角色和菜单的对应关系
        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id", role.getRoleId()));

        //保存角色和菜单的对应关系
        Set<Long> menuList = role.getMenuIdList();
        if (menuList != null && menuList.size() > 0) {
            List<RoleMenu> roleMenus = new ArrayList<>();
            for (Long menu : menuList) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(role.getRoleId());
                roleMenu.setMenuId(menu);
                roleMenus.add(roleMenu);
            }

            roleMenuService.saveBatch(roleMenus);
        }

        return R.success("修改成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("sys:role:delete")
    public R delete(@PathVariable("id") Long id) {
        roleService.removeById(id);

        //删除角色和菜单的对应关系
        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id", id));

        return R.success("删除成功");
    }
}
