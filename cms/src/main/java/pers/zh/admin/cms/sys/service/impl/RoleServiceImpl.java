package pers.zh.admin.cms.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.zh.admin.cms.sys.dao.RoleDao;
import pers.zh.admin.cms.sys.entity.Role;
import pers.zh.admin.cms.sys.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 18:46
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {
}
