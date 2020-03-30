package pers.zh.admin.cms.common.utlis;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 15:23
 */
public interface Constant {

    /**
     * 超级管理员ID
     */
    Long SUPER_ADMIN = 1L;

    /**
     * 数据权限过滤
     */
    String SQL_FILTER = "sql_filter";

    /**
     * 当前页码
     */
    String PAGE = "page";

    /**
     * 每页显示记录数
     */
    String SIZE = "size";

    /**
     * 排序字段
     */
    String ORDER_FIELD = "sidx";

    /**
     * 排序方式
     */
    String ORDER = "order";

    /**
     * 升序
     */
    String ASC = "asc";

    /**
     * 菜单类型
     */
    enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
