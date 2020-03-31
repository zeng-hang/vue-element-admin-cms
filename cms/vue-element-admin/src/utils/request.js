import axios from 'axios'
import {Message} from 'element-ui'
import store from '@/store'
import {removeAll} from '@/utils/auth'

//是否取消请求的标识
let hasCancel = false;

// create an axios instance
const service = axios.create({
    baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
    // withCredentials: true, // 跨域请求时发送cookies，我这里使用了代理不需要跨域
    timeout: 5000 // 请求超时时间（毫秒）
})

// 请求拦截器
service.interceptors.request.use(
    config => {
        //如果你想在请求之间做一些其他的操作可以在这里进行

        // 这是一个在请求之前往请求头添加一个信息的示例
        // if (store.getters.token) {
        //   // 让每个请求携带令牌
        //   // ['X-Token'] is a custom headers key
        //   // please modify it according to the actual situation
        //   config.headers['X-Token'] = getToken()
        // }

        if (hasCancel) return Promise.reject(new Error('cancel'));

        return config
    },
    error => {
        // do something with request error
        console.log(error) // for debug
        return Promise.reject(error)
    }
)

/**
 * http响应拦截器
 */
service.interceptors.response.use(
    /**
     * 如果你想获得http的头部或者状态信息可直接将response返回
     */

    /**
     * todo 这里可自定义通过后台返回的状态码来判断不同的响应状态
     */
    response => {
        const res = response.data

        //一般的情况下后台都是会返回一个code的
        //如果没有code可能是需要直接处理的信息，这里直接返回
        if (res.code == null) {
            return res;
        }

        //后台默认成功code码为200，不为200都是错误的请求
        if (res.code !== 200) {
            Message({
                message: res.message || res.msg || 'Error',
                type: 'error',
                duration: 5 * 1000
            })
            return Promise.reject(new Error(res.message || res.msg || 'Error'))
        } else {
            return res
        }
    },
    /**
     * todo 这里是通过http状态码来判断响应状态
     */
    error => {
        if (axios.isCancel(error)) {
            removeAll()
            return Promise.reject(error);
        } else if (error.message === 'cancel') {
            removeAll()
            return Promise.reject(error);
        } else if (error && error.response) {
            switch (error.response.status) {
                case 400:
                    error.msg = '请求错误'
                    break
                case 401:
                    removeAll()
                    hasCancel = true;
                    error.msg = '登录凭证已失效，请重新登录'
                    setTimeout(() => {
                        store.dispatch('user/resetToken').then(() => {
                            location.reload()
                        })
                    }, 2000)
                    break
                case 403:
                    removeAll()
                    hasCancel = true;
                    error.msg = '您还未登录，登录体验完整功能'
                    setTimeout(() => {
                        store.dispatch('user/resetToken').then(() => {
                            location.reload()
                        })
                    }, 2000)
                    break
                case 404:
                    error.msg = `请求地址出错: ${error.response.config.url}`
                    break
                case 408:
                    error.msg = '请求超时'
                    break
                case 500:
                    if (error.response.data.status !== undefined && error.response.data.status !== 500) {
                        error.msg = error.response.data.message
                    } else {
                        error.msg = '服务器内部错误'
                    }
                    break
                case 501:
                    error.msg = '服务未实现'
                    break
                case 502:
                    error.msg = '网关错误'
                    break
                case 503:
                    error.msg = '服务不可用'
                    break
                case 504:
                    error.msg = '网关超时'
                    break
                case 505:
                    error.msg = 'HTTP版本不受支持'
                    break
                default:
                    error.msg = '请求出现异常'
            }
        } else {
            error.code = 500
            error.msg = '请求错误'
        }

        Message({
            message: error.msg,
            type: 'error',
            duration: 5 * 1000
        })
        return Promise.reject(error)
    }
)

export default service
