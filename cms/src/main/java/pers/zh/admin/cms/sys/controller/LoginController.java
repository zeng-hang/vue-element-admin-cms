package pers.zh.admin.cms.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.zh.admin.cms.common.utlis.R;
import pers.zh.admin.cms.sys.entity.Role;
import pers.zh.admin.cms.sys.entity.User;
import pers.zh.admin.cms.sys.service.RoleService;
import pers.zh.admin.cms.sys.service.UserRoleService;
import pers.zh.admin.cms.sys.shiro.ShiroUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 19:57
 */
@RestController
public class LoginController {
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public R login(String username, String password) {
        try {
            Subject subject = ShiroUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);

            User user = (User) subject.getPrincipal();
            R r = R.success().put("JSESSIONID", subject.getSession().getId());

            List<Long> roleIds = userRoleService.queryRoleIdList(user.getUserId());
            r.put("roles", roleService.list(
                    new QueryWrapper<Role>()
                            .in("role_id", roleIds)
                    )
                            .stream()
                            .map(Role::getRole)
                            .collect(Collectors.toList())
            );

            r.put("data", user);
            return r;
        } catch (UnknownAccountException e) {
            return R.error(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            return R.error("账号或密码不正确");
        } catch (LockedAccountException e) {
            return R.error("账号已被锁定,请联系管理员");
        } catch (AuthenticationException e) {
            return R.error("账户验证失败");
        }
    }

    /**
     * 退出登录
     */
    @GetMapping("logout")
    public R logout() {
        ShiroUtils.logout();
        return R.success("退出成功");
    }

}
