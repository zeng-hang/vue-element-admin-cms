package pers.zh.admin.cms.sys.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import pers.zh.admin.cms.sys.entity.Menu;

import java.util.List;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 18:25
 */
public interface MenuService extends IService<Menu> {
    List<Menu> selectList(Wrapper<Menu> wrapper);

    List<String> getPermission(Long userId);

    List<Menu> getUserMenuList(Long userId);
}
