package pers.zh.admin.cms.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 数据字典
 * Created by zenghang.
 * Date: 2020/4/10
 * Time: 14:17
 */
@Data
@TableName("sys_dict")
public class Dict {
    @TableId
    private Integer dictId;

    /**
     * 字典值，主显示
     */
    private String label;

    /**
     * 字典码
     */
    private String code;

    /**
     * 父级id，没有父级则为0
     */
    private Integer parentId;

    /**
     * 排序
     */
    private Integer sortNo;
}
