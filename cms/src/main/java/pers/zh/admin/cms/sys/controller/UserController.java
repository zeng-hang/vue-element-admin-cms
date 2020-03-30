package pers.zh.admin.cms.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import pers.zh.admin.cms.common.utlis.Query;
import pers.zh.admin.cms.common.utlis.R;
import pers.zh.admin.cms.sys.entity.User;
import pers.zh.admin.cms.sys.entity.UserRole;
import pers.zh.admin.cms.sys.service.UserRoleService;
import pers.zh.admin.cms.sys.service.UserService;
import pers.zh.admin.cms.sys.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 15:18
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:user:list")
    public R list(@RequestParam Map<String, Object> params) {
        IPage<User> page = userService.page(
                new Query<User>().getPage(params),
                new QueryWrapper<User>()
                        .like(StringUtils.isNotBlank(String.valueOf(params.get("userName"))), "user_name", params.get("userName"))
                        .like(StringUtils.isNotBlank(String.valueOf(params.get("nickname"))), "nickname", params.get("nickname"))
                        .like(StringUtils.isNotBlank(String.valueOf(params.get("phone"))), "phone", params.get("phone"))
                        .like(StringUtils.isNotBlank(String.valueOf(params.get("mail"))), "mail", params.get("mail"))
        );
        return R.success().put("data", page);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:user:info")
    public R info(@PathVariable("id") Long id) {
        return R.success().put("data", userService.getById(id));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:user:save")
    public R save(@RequestBody User user) {
        userService.register(user);
        return R.success("保存成功");
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:user:update")
    public R update(@RequestBody User user) {
        user.setPassword(null);
        userService.updateById(user);
        return R.success("修改成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("sys:user:delete")
    public R delete(@PathVariable("id") Long id) {
        userService.removeById(id);

        //删除对应的角色关系
        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", id));

        return R.success("删除成功");
    }

    /**
     * 修改密码
     */
    @PostMapping("/modify/password")
    public R modifyPassword(String password, String oldPassword) {
        User user = ShiroUtils.getUserEntity();

        oldPassword = ShiroUtils.sha256(oldPassword, user.getSign());
        if (!user.getPassword().equals(oldPassword)) {
            return R.error("旧密码有误");
        }

        user.setPassword(password);

        userService.updateById(user);

        return R.success("修改密码成功");
    }

    /**
     * 用户角色
     */
    @GetMapping("/role/list/{userId}")
    public R role(@PathVariable("userId") Long userId) {
        return R.success().put("data", userRoleService.queryRoleIdList(userId));
    }
}
