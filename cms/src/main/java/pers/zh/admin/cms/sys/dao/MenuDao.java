package pers.zh.admin.cms.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import pers.zh.admin.cms.sys.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 18:25
 */
@Mapper
public interface MenuDao extends BaseMapper<Menu> {

    /**
     * 获取用户权限
     * @param userId
     * @param type
     * @return
     */
    List<Menu> getUserMenuList(@Param("userId") Long userId, @Param("type") String type);
}
