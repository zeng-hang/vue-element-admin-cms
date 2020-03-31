import request from '@/utils/request'
import {getPerm} from '@/utils/auth'

export function login(data) {
    return request({
        url: '/login',
        method: 'post',
        params: data
    })
}

export function getPerms() {
    const perms = getPerm();
    if (perms !== null && perms.length > 0) {
        return new Promise((resolve) => {
            resolve(perms)
        });
    }

    return request({
        url: '/menu/perms',
        method: 'get'
    })
}

export function logout() {
    return request({
        url: '/logout',
        method: 'get'
    })
}
