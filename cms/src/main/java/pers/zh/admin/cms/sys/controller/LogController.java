package pers.zh.admin.cms.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import pers.zh.admin.cms.common.utlis.Query;
import pers.zh.admin.cms.common.utlis.R;
import pers.zh.admin.cms.sys.entity.Log;
import pers.zh.admin.cms.sys.service.LogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 20:44
 */
@RestController
@RequestMapping("/log")
public class LogController {
    @Autowired
    private LogService logService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:log:list")
    public R list(@RequestParam Map<String, Object> params) {
        IPage<Log> page = logService.page(new Query<Log>().getPage(params), new QueryWrapper<>());
        return R.success().put("data", page);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:log:info")
    public R info(@PathVariable("id") Long id) {
        return R.success().put("data", logService.getById(id));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:log:save")
    public R save(@RequestBody Log log) {
        logService.save(log);
        return R.success("保存成功");
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:log:update")
    public R update(@RequestBody Log log) {
        logService.updateById(log);
        return R.success("修改成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("sys:log:delete")
    public R delete(@PathVariable("id") Long id) {
        logService.removeById(id);
        return R.success("删除成功");
    }
}
