import {asyncRoutes, constantRoutes} from '@/router'
import {setMenuNav} from '@/utils/auth'
import {menuNav} from '@/api/menu'
import {deleteNullNode} from '@/utils/filter-tree'
import Layout from '@/layout'
import AppMain from '@/layout/components/AppMain.vue'

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
    if (route.meta && route.meta.roles) {
        return roles.some(role => route.meta.roles.includes(role))
    } else {
        return true
    }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
    const res = []

    routes.forEach(route => {
        const tmp = {...route}
        if (hasPermission(roles, tmp)) {
            if (tmp.children) {
                tmp.children = filterAsyncRoutes(tmp.children, roles)
            }
            res.push(tmp)
        }
    })

    return res
}

const state = {
    routes: [],
    addRoutes: []
}

const mutations = {
    SET_ROUTES: (state, routes) => {
        state.addRoutes = routes
        state.routes = constantRoutes.concat(asyncRoutes, routes)
    }
}

const actions = {
    generateRoutes({commit}) {
        return new Promise((resolve, reject) => {
            menuNav().then(({data}) => {
                setMenuNav(data)
                let accessedRoutes = handleRouter(data)
                deleteNullNode(accessedRoutes);
                accessedRoutes.push({
                    path: '*',
                    component: () => import('@/views/error-page/404'),
                    hidden: true
                })
                commit('SET_ROUTES', accessedRoutes)
                resolve(accessedRoutes)
            }).catch(err => {
                reject(err)
            })

        })
    }
}

/**
 * 组装路由表
 * @param routerData 后台返回的树形菜单
 */
function handleRouter(routerData) {
    return routerData.map(item => {
        let data = item.data;
        let route = {
            path: data.url,
            redirect: data.redirect,
            hidden: !data.isHidden,
            alwaysShow: data.waysShow,
            name: data.routeName,
            component: () => import(`@/views${data.component}`),
            meta: {
                title: data.title,
                icon: data.icon,
                noCache: data.isCache
            }
        };

        if (data.component.indexOf('Layout') !== -1) {
            route.component = Layout;
        } else if (data.component.indexOf("AppMain") !== -1) {
            route.component = AppMain;
        }
        if (item.children) {
            route.children = handleRouter(item.children)
        }
        return route;
    })
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
