/**
 * 删除树形菜单中children为null | undefined | length === 0 的子级
 * @param nodes
 */
export function deleteNullNode(nodes) {
    'use strict';
    for (let node of nodes) {
        if (node.children != null && node.children.length > 0) {
            deleteNullNode(node.children)
        } else {
            delete node.children
        }
    }
}

/**
 * 通过给定的id找到全路径
 * @param id
 * @param idPath
 * @param data
 * @returns {string}
 */
export function findIdPath(id, data = [], idPath = '') {
    for (let item of data) {
        if (item.id === id + '') {
            return idPath + ',' + item.id;
        }
        if (item.children) {
            idPath = findIdPath(id, item.children, idPath);
        }
        if (idPath !== '') {
            return idPath += ',' + item.id;
        }
    }

    if (idPath == null) {
        return '';
    } else {
        return idPath;
    }
}
