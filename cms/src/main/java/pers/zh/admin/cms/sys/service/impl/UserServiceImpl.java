package pers.zh.admin.cms.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.zh.admin.cms.sys.dao.UserDao;
import pers.zh.admin.cms.sys.entity.User;
import pers.zh.admin.cms.sys.entity.UserRole;
import pers.zh.admin.cms.sys.service.UserRoleService;
import pers.zh.admin.cms.sys.service.UserService;
import pers.zh.admin.cms.sys.shiro.ShiroUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 15:17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public List<String> queryAllPerms(Long userId) {
        return baseMapper.queryAllPerms(userId);
    }

    @Override
    public User register(User user) {
        //sha256加密
        user.setSign(RandomStringUtils.randomAlphanumeric(20));
        user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSign()));

        this.save(user);

        //保存对应的角色关系
        if (user.getRolesList() != null && user.getRolesList().size() > 0) {
            List<UserRole> roles = new ArrayList<>();
            for (Integer role : user.getRolesList()) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(role);
                userRole.setUserId(user.getUserId());
            }

            userRoleService.saveBatch(roles);
        }

        return user;
    }

    @Override
    public boolean updateById(User user) {
        if (user.getPassword() != null) {
            user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSign()));
        }

        //删除对应的角色关系
        userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", user.getUserId()));

        //保存对应的角色关系
        if (user.getRolesList() != null && user.getRolesList().size() > 0) {
            List<UserRole> roles = new ArrayList<>();
            for (Integer role : user.getRolesList()) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(role);
                userRole.setUserId(user.getUserId());
                roles.add(userRole);
            }

            userRoleService.saveBatch(roles);
        }

        return super.updateById(user);
    }
}
