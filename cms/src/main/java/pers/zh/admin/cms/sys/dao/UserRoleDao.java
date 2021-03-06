package pers.zh.admin.cms.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import pers.zh.admin.cms.sys.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 18:53
 */
@Mapper
public interface UserRoleDao extends BaseMapper<UserRole> {

    List<Long> queryRoleIdList(Long userId);
}
