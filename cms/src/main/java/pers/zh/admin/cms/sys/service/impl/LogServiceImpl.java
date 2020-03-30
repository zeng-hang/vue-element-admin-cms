package pers.zh.admin.cms.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import pers.zh.admin.cms.sys.dao.LogDao;
import pers.zh.admin.cms.sys.entity.Log;
import pers.zh.admin.cms.sys.service.LogService;
import org.springframework.stereotype.Service;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 20:34
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogDao, Log> implements LogService {
}
