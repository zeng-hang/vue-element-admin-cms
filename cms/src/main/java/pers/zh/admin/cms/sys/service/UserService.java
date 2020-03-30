package pers.zh.admin.cms.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.zh.admin.cms.sys.entity.User;

import java.util.List;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 15:16
 */
public interface UserService extends IService<User> {
    List<String> queryAllPerms(Long userId);

    User register(User user);
}
