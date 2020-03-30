package pers.zh.admin.cms.common.utlis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * Created by zenghang.
 * Date: 2020/1/10
 * Time: 17:49
 */
@Component
public class FileUtils {
    /**
     * 文件上传地址
     */
    @Value("${file.upload.path}")
    public String filePath;

    /**
     * 文件上传
     *
     * @param file     文件二进制
     * @param fileName 文件名
     * @throws Exception
     */
    public void uploadFile(byte[] file, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名称
     * @return
     */
    public boolean deleteFile(String fileName) {
        File file = new File(filePath + fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * 生成保存的文件名
     *
     * @param suffix 文件后缀名
     * @return
     */
    public String renameToUUID(String suffix) {
        return UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
    }
}
