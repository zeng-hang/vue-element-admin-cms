package pers.zh.admin.cms.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import pers.zh.admin.cms.common.utlis.R;
import pers.zh.admin.cms.sys.entity.User;
import pers.zh.admin.cms.sys.model.ForgetPasswordModel;
import pers.zh.admin.cms.sys.service.UserService;
import pers.zh.admin.cms.sys.shiro.ShiroUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 19:57
 */
@RestController
public class LoginController {
    @Autowired
    private UserService userService;

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
     * 注册
     */
    @PostMapping("/register")
    public R register(@RequestBody User user) {
        userService.register(user);
        return R.success("注册成功");
    }

    /**
     * 退出登录
     */
    @GetMapping("logout")
    public R logout() {
        ShiroUtils.logout();
        return R.success("退出成功");
    }

    /**
     * 忘记密码
     */
    @PostMapping("/forget/password")
    public R forgetPassword(@RequestBody ForgetPasswordModel forgetPasswordModel) {
        if (forgetPasswordModel.getType() == 1) {
            //手机号验证
            User user = userService.getOne(
                    new QueryWrapper<User>()
                            .eq("user_name", forgetPasswordModel.getUserName())
                            .eq("phone", forgetPasswordModel.getPhone())
            );

            if (user == null) {
                return R.error("用户验证失败，请检查输入的手机号和用户名是否正确");
            }

            userService.updateById(user);

        } else if (forgetPasswordModel.getType() == 2) {
            //邮箱验证
            User user = userService.getOne(
                    new QueryWrapper<User>()
                            .eq("user_name", forgetPasswordModel.getUserName())
                            .eq("mail", forgetPasswordModel.getPhone())
            );

            if (user == null) {
                return R.error("用户验证失败，请检查输入的邮箱和用户名是否正确");
            }

            userService.updateById(user);

        } else {
            return R.error("验证方式错误");
        }

        return R.success("已成功找回密码");
    }
}
