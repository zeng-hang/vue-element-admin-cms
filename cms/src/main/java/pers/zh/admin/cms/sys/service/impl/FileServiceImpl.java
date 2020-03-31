package pers.zh.admin.cms.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pers.zh.admin.cms.sys.dao.FileDao;
import pers.zh.admin.cms.sys.entity.FileEntity;
import pers.zh.admin.cms.sys.service.FileService;

/**
 * Created by zenghang.
 * Date: 2020/1/10
 * Time: 17:10
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileDao, FileEntity> implements FileService {
}
