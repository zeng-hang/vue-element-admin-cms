import store from '@/store'

/**
 * 权限控制访问函数
 * @param {Array} value
 * @returns {Boolean}
 */
export function checkPermission(value) {
    //默认不给权限标识直接放过
    if (value && value instanceof Array && value.length > 0) {
        const perms = store.getters && store.getters.perms

        //默认一个权限都没有直接放过
        if (perms == null) return true;

        const permissions = value

        return perms.some(per => {
            return permissions.includes(per)
        });

    }
    return true;
}

