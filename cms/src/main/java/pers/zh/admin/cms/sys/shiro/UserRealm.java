package pers.zh.admin.cms.sys.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import pers.zh.admin.cms.common.utlis.Constant;
import pers.zh.admin.cms.sys.entity.Menu;
import pers.zh.admin.cms.sys.entity.User;
import pers.zh.admin.cms.sys.service.MenuService;
import pers.zh.admin.cms.sys.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * Shiro登录认证自定义类
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 15:05
 */
@Component
public class UserRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;
    @Resource
    private MenuService menuService;

    /**
     * 授权，访问接口时调用
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        Long userId = user.getUserId();

        List<String> permsList;
        //系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            List<Menu> menuList = menuService.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for (Menu menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = userService.queryAllPerms(userId);
        }

        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        //查询用户信息
        User user = userService.getOne(new QueryWrapper<User>().eq("user_name", token.getUsername()));

        //账号不存在
        if (user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }

        //账号锁定
        if (user.getStatus() == User.STATUS.LOCK) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSign()), getName());
        return info;
    }

    /**
     * 自定义密码加密验证
     * @param credentialsMatcher
     */
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
        shaCredentialsMatcher.setHashAlgorithmName(ShiroUtils.hashAlgorithmName);
        shaCredentialsMatcher.setHashIterations(ShiroUtils.hashIterations);
        super.setCredentialsMatcher(shaCredentialsMatcher);
    }
}
