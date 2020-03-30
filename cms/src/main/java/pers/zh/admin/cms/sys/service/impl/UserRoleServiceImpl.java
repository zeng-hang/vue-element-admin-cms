package pers.zh.admin.cms.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.zh.admin.cms.sys.dao.UserRoleDao;
import pers.zh.admin.cms.sys.entity.UserRole;
import pers.zh.admin.cms.sys.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 18:52
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRole> implements UserRoleService {

    @Override
    public List<Long> queryRoleIdList(Long userId) {
        return baseMapper.queryRoleIdList(userId);
    }
}
