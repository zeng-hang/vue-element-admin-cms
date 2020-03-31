import {login, logout, getPerms} from '@/api/user'
import {
    setUser,
    getUser,
    setRoles,
    getRoles,
    setPerm,
    removePerm,
    removeAll
} from '@/utils/auth'
import router, {resetRouter} from '@/router'

const state = {
    user: {},
    perms: [],
    roles: []
}

const mutations = {
    SET_USER: (state, user) => {
        state.user = user
    },
    SET_PERMS: (state, perms) => {
        state.perms = perms
    },
    SET_ROLES: (state, roles) => {
        state.roles = roles
    },
}

const actions = {
    // user login
    login({commit}, userInfo) {
        const {username, password} = userInfo
        return new Promise((resolve, reject) => {
            login({username: username.trim(), password: password}).then(response => {
                const {data, roles} = response

                //登录成功返回的用户信息
                commit('SET_USER', data);
                setUser(data);

                commit('SET_ROLES', roles);
                setRoles(roles);

                //登录成功之后清除权限信息，重新拉取信息
                commit('SET_PERMS', []);
                removePerm();
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    // get user info
    getInfo({commit, state}) {
        return new Promise((resolve, reject) => {
            getPerms(state.token).then(({data}) => {
                commit('SET_USER', getUser());
                commit('SET_ROLES', getRoles());

                commit('SET_PERMS', data);
                setPerm(data);

                resolve(data)
            }).catch(error => {
                reject(error)
            })
        })
    },

    // user logout
    logout({commit, state, dispatch}) {
        return new Promise((resolve, reject) => {
            logout(state.token).then(() => {
                resetRouter()
                removeAll()
                // reset visited views and cached views
                // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
                dispatch('tagsView/delAllViews', null, {root: true})

                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },

    // remove token
    resetToken({commit}) {
        return new Promise(resolve => {
            removeAll()
            resolve()
        })
    },

    // dynamically modify permissions
    changeRoles({commit, dispatch}, role) {
        return new Promise(async resolve => {
            await dispatch('getInfo')

            resetRouter()

            // generate accessible routes map based on roles
            const accessRoutes = await dispatch('permission/generateRoutes', {root: true})

            // dynamically add accessible routes
            router.addRoutes(accessRoutes)

            // reset visited views and cached views
            dispatch('tagsView/delAllViews', null, {root: true})

            resolve()
        })
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
