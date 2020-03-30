package pers.zh.admin.cms.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.zh.admin.cms.common.utlis.Constant;
import pers.zh.admin.cms.sys.dao.MenuDao;
import pers.zh.admin.cms.sys.entity.Menu;
import pers.zh.admin.cms.sys.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 18:26
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuDao, Menu> implements MenuService {
    @Override
    public List<Menu> selectList(Wrapper<Menu> wrapper) {
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<String> getPermission(Long userId) {
        Set<String> perms = new HashSet<>();

        List<Menu> list;
        if (!userId.equals(Constant.SUPER_ADMIN)) {
            list = this.baseMapper.getUserMenuList(userId, "");
        } else {
            //超级管理员拥有所有权限
            list = this.baseMapper.selectList(null);
        }

        //通过HashSet去重
        list.forEach(m -> {
            if (m.getPerms() != null)
                perms.addAll(new ArrayList<>(Arrays.asList(m.getPerms().split(","))));
        });

        return new ArrayList<>(perms);
    }

    @Override
    public List<Menu> getUserMenuList(Long userId) {
        //系统管理员，拥有最高权限
        if (userId.equals(Constant.SUPER_ADMIN)) {
            return list(new QueryWrapper<Menu>().in("type", "0", "1"));
        }

        //用户所有的菜单列表
        List<Menu> list = this.baseMapper.getUserMenuList(userId, "");

        //找到对应的父级节点
        List<Menu> data = findParentMenu(list);
        list.addAll(data);
        data = data.stream().filter(d -> !d.getParentId().equals(0L)).collect(Collectors.toList());

        while (data.size() > 0) {
            data = findParentMenu(data);
            list.addAll(data);
            data = data.stream().filter(d -> !d.getParentId().equals(0L)).collect(Collectors.toList());
        }

        //去重
        List<Menu> finalList = new ArrayList<>();
        list.forEach(l -> {
            if (!finalList.contains(l)) {
                finalList.add(l);
            }
        });

        return finalList;
    }

    /**
     * 根据element-ui 的 tree组件逻辑
     * 如果子级不是全选父级是没有被选中的
     * 该方法就是为了找到父级节点
     *
     * @param list
     * @return
     */
    private List<Menu> findParentMenu(List<Menu> list) {
        List<Menu> data = new ArrayList<>();
        Set<Long> set = new HashSet<>();
        list.forEach(l -> set.add(l.getParentId()));

        set.forEach(id -> {
            if (id != 0)
                data.add(baseMapper.selectById(id));
        });

        return data;
    }
}
