// import Cookies from 'js-cookie'

const cacheList = {
    UserInfo: 'User-Info',
    Roles: 'Roles',
    Perms: 'Perms',
    MenuNav: 'MenuNav'
}

//用户基本信息相关缓存
export function setUser(user = {}) {
    localStorage.setItem(cacheList.UserInfo, JSON.stringify(user));
}

export function getUser() {
    return JSON.parse(localStorage.getItem(cacheList.UserInfo) || '{}')
}

export function removeUser() {
    localStorage.removeItem(cacheList.UserInfo)
}


//用户角色信息相关缓存
export function setRoles(roles = []) {
    localStorage.setItem(cacheList.Roles, JSON.stringify(roles));
}

export function getRoles() {
    return JSON.parse(localStorage.getItem(cacheList.Roles) || '[]')
}

export function removeRoles() {
    localStorage.removeItem(cacheList.Roles)
}


//用户权限相关缓存
export function setPerm(perms = []) {
    localStorage.setItem(cacheList.Perms, JSON.stringify(perms))
}

export function getPerm() {
    return JSON.parse(localStorage.getItem(cacheList.Perms) || '[]')
}

export function removePerm() {
    localStorage.removeItem(cacheList.Perms)
}


//用户菜单相关缓存
export function setMenuNav(perms = []) {
    localStorage.setItem(cacheList.MenuNav, JSON.stringify(perms))
}

export function getMenuNav() {
    return JSON.parse(Cookies.getItem(cacheList.MenuNav) || '[]')
}

export function removeMenuNav() {
    localStorage.removeItem(cacheList.MenuNav)
}

/**
 * 清除所有缓存
 */
export function removeAll() {
    for (let key in cacheList) {
        localStorage.removeItem(cacheList[key])
    }
}

