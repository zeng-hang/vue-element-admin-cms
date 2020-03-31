package pers.zh.admin.cms.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Created by zenghang.
 * Date: 2020/1/10
 * Time: 17:03
 */
@Data
@TableName("SYS_FILE")
public class FileEntity {
    @TableId
    private Long fileId;

    /**
     * 文件源名称
     */
    private String fileName;

    /**
     * 文件保存名
     */
    private String storageName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 上传人
     */
    private Long userId;

    /**
     * 上传时间
     */
    private Date createTime = new Date();

    /**
     * 下载地址
     */
    private String downloadUrl;

}
