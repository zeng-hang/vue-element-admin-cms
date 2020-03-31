package pers.zh.admin.cms.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.zh.admin.cms.common.utlis.FileUtils;
import pers.zh.admin.cms.common.utlis.Query;
import pers.zh.admin.cms.common.utlis.R;
import pers.zh.admin.cms.sys.entity.FileEntity;
import pers.zh.admin.cms.sys.service.FileService;
import pers.zh.admin.cms.sys.shiro.ShiroUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * Created by zenghang.
 * Date: 2020/1/10
 * Time: 17:11
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private FileUtils fileUtils;

    @Value("${server.servlet.context-path}")
    private String contentPath;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:file:list")
    public R list(@RequestParam Map<String, Object> params) {
        IPage<FileEntity> page = fileService.page(
                new Query<FileEntity>().getPage(params),
                new QueryWrapper<FileEntity>()
                        .like(StringUtils.isNotBlank((String) params.get("fileName")), "file_name", params.get("fileName"))
                        .like(StringUtils.isNotBlank((String) params.get("fileType")), "file_type", params.get("fileType"))
                        .like(StringUtils.isNotBlank((String) params.get("used")), "used", params.get("used"))
        );
        return R.success().put("data", page);
    }

    /**
     * 上传
     */
    @RequestMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file) {
        String url;
        try {
            String fileName = file.getOriginalFilename();
            String suffix = StringUtils.substringAfter(fileName, ".");

            if (suffix == null || "".equals(suffix)) {
                suffix = "jpeg";
            }

            String storageName = fileUtils.renameToUUID(suffix);

            fileUtils.uploadFile(file.getBytes(), storageName);

            //存数据库
            FileEntity fileEntity = new FileEntity();
            fileEntity.setUserId(ShiroUtils.getUserId());
            fileEntity.setFileName(fileName);
            fileEntity.setStorageName(storageName);
            fileEntity.setFileType(suffix);
            fileEntity.setSize(file.getSize());
            fileEntity.setDownloadUrl(contentPath + "/path/" + storageName);
            url = fileEntity.getDownloadUrl();
            fileService.save(fileEntity);

        } catch (Exception e) {
            e.printStackTrace();
            return R.error("上传异常");
        }

        return R.success().put("data", url);
    }

    /**
     * 上传
     */
    @RequestMapping("/uploads")
    public R uploads(@RequestParam("files") MultipartFile[] file) {
        StringBuilder failFile = new StringBuilder();
        int failNum = 0;
        for (MultipartFile multipartFile : file) {
            R r = upload(multipartFile);
            if (200 != (int) r.get("code")) {
                String fileName = multipartFile.getOriginalFilename();
                failFile.append(multipartFile.getOriginalFilename()).append(",");
                failNum++;
            }
        }

        if (failNum == 0) {
            return R.success("上传成功");
        } else if (failNum == file.length) {
            return R.error("文件上传失败");
        }
        return R.success("部分文件上传成功，" + failFile + "文件上传失败").put("failList", failFile.toString().split(","));
    }

    /**
     * 文件下载
     *
     * @param fileId
     * @param response
     */
    @GetMapping("/download/{fileId}")
    public void download(@PathVariable("fileId") Long fileId, HttpServletResponse response) {
        FileEntity fileEntity = fileService.getById(fileId);
        if (fileEntity == null) {
            return;
        }
        //设置文件路径
        File file = new File(fileUtils.filePath + fileEntity.getStorageName());
        System.out.println(file.getPath());
        //File file = new File(realPath , fileName);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileEntity.getFileName() + "." + fileEntity.getFileType());// 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    @RequiresPermissions("sys:file:delete")
    public R delete(@PathVariable("id") Long id) {
        FileEntity fileEntity = fileService.getById(id);
        if (fileEntity == null) {
            return R.error("删除失败");
        }

        if (fileUtils.deleteFile(fileEntity.getStorageName())) {
            fileService.removeById(id);
            return R.success("删除成功");
        }

        return R.error("删除失败");
    }

    /**
     * 批量删除
     */
    @PostMapping("/batch/delete")
    @RequiresPermissions("sys:file:delete")
    public R batchDelete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
        return R.success("删除成功");
    }
}
