package pers.zh.admin.cms.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.zh.admin.cms.sys.entity.UserRole;

import java.util.List;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 18:52
 */
public interface UserRoleService extends IService<UserRole> {
    List<Long> queryRoleIdList(Long userId);
}
