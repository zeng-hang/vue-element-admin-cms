package pers.zh.admin.cms.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pers.zh.admin.cms.sys.dao.DictDao;
import pers.zh.admin.cms.sys.entity.Dict;
import pers.zh.admin.cms.sys.service.DictService;

/**
 * Created by zenghang.
 * Date: 2020/1/10
 * Time: 17:10
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictDao, Dict> implements DictService {
}
