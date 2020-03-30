package pers.zh.admin.cms.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import pers.zh.admin.cms.sys.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 15:18
 */
@Mapper
public interface UserDao extends BaseMapper<User> {
    List<String> queryAllPerms(Long userId);
}
