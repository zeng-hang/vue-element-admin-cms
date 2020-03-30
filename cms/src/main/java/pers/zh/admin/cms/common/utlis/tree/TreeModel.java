package pers.zh.admin.cms.common.utlis.tree;

import lombok.Data;

import java.util.List;

/**
 * Created by zenghang.
 * Date: 2020/1/9
 * Time: 17:43
 */
@Data
public class TreeModel {
    //基础属性
    /**
     * 主键ID，应为唯一，可以作为子级的parentId
     */
    private String id;

    /**
     * 基础字段
     */
    private String name;

    /**
     * 父级的特征值，可以为id，也可以为parentKey
     */
    private String parentId;

    /**
     * 特征值，如果使用id作为parentId该字段和id相同即可
     */
    private String parentKey;

    /**
     * 子级
     */
    private List<TreeModel> children;

    /**
     * 完整数据
     */
    private Object data;

    //element tree 属性
    /**
     * 指定节点选择框是否禁用为节点对象的某个属性值
     */
    private Boolean disabled;

}
