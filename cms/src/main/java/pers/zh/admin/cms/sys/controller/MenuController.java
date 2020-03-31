package pers.zh.admin.cms.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import pers.zh.admin.cms.common.utlis.Query;
import pers.zh.admin.cms.common.utlis.R;
import pers.zh.admin.cms.common.utlis.tree.TreeModel;
import pers.zh.admin.cms.common.utlis.tree.TreeUtil;
import pers.zh.admin.cms.sys.entity.Menu;
import pers.zh.admin.cms.sys.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static pers.zh.admin.cms.sys.shiro.ShiroUtils.getUserId;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 18:25
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:menu:list")
    public R list(@RequestParam Map<String, Object> params) {
        IPage<Menu> page = menuService.page(new Query<Menu>().getPage(params), new QueryWrapper<>());
        return R.success().put("data", page);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:menu:info")
    public R info(@PathVariable("id") Long id) {
        return R.success().put("data", menuService.getById(id));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:menu:save")
    public R save(@RequestBody Menu menu) {
        menuService.save(menu);
        return R.success("保存成功");
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:menu:update")
    public R update(@RequestBody Menu menu) {
        menuService.updateById(menu);
        return R.success("修改成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("sys:menu:delete")
    public R delete(@PathVariable("id") Long id) {
        menuService.removeById(id);
        return R.success("删除成功");
    }

    /**
     * 获取用户权限信息
     */
    @GetMapping("/perms")
    public R perms() {
        return R.success().put("data", menuService.getPermission(getUserId()));
    }

    /**
     * 获取用户菜单
     */
    @GetMapping("/tree/nav")
    public R treeNav() {
        List<Menu> menuList = menuService.getUserMenuList(getUserId());

        List<TreeModel> trees = new ArrayList<>();
        menuList.forEach(menu -> {
            if (!(menu.getType() == 2)) {
                TreeModel tree = new TreeModel();
                tree.setId(menu.getMenuId() + "");
                tree.setName(menu.getTitle());
                tree.setParentId(menu.getParentId() + "");
                tree.setParentKey(menu.getMenuId() + "");
                tree.setData(menu);
                trees.add(tree);
            }
        });

        return R.success().put("data", TreeUtil.loadTree(trees, "0"));
    }


    /**
     * 获取树形菜单
     *
     * @return
     */
    @RequestMapping("/tree")
    public R tree() {
        List<Menu> menuList = menuService.list();
        List<TreeModel> trees = new ArrayList<>();
        menuList.forEach(meun -> {
            TreeModel tree = new TreeModel();
            tree.setId(meun.getMenuId() + "");
            tree.setName(meun.getTitle());
            tree.setParentId(meun.getParentId() + "");
            tree.setParentKey(meun.getMenuId() + "");
            tree.setData(meun);
            trees.add(tree);
        });

        return R.success().put("data", TreeUtil.loadTree(trees, "0"));
    }
}
