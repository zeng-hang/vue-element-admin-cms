package pers.zh.admin.cms.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.zh.admin.cms.common.utlis.Query;
import pers.zh.admin.cms.common.utlis.R;
import pers.zh.admin.cms.sys.entity.Dict;
import pers.zh.admin.cms.sys.service.DictService;

import java.util.List;
import java.util.Map;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 20:44
 */
@RestController
@RequestMapping("/dict")
public class DictController {
    @Autowired
    private DictService dictService;

    /**
     * 列表
     */
    @GetMapping("/list/{id}")
    @RequiresPermissions("sys:dict:list")
    public R list(@PathVariable("id") Integer id) {
        List<Dict> list = dictService.list(new QueryWrapper<Dict>().eq("parent_id", id).orderByDesc("sort_no"));
        return R.success().put("data", list);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:dict:info")
    public R info(@PathVariable("id") Integer id) {
        return R.success().put("data", dictService.getById(id));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:dict:save")
    public R save(@RequestBody Dict dict) {
        dictService.save(dict);
        return R.success("保存成功");
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:dict:update")
    public R update(@RequestBody Dict dict) {
        dictService.updateById(dict);
        return R.success("修改成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("sys:dict:delete")
    public R delete(@PathVariable("id") Integer id) {
        Dict dict = dictService.getById(id);
        if (dict.getParentId() == 0) {
            //字典目录删除所有的字典项
            dictService.remove(new QueryWrapper<Dict>().eq("parent_id", dict.getDictId()));
        }

        dictService.removeById(id);
        return R.success("删除成功");
    }
}
