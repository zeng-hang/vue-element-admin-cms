import Vue from 'vue'
import SvgIcon from '@/components/SvgIcon'// svg component
import IconList from '@/components/SvgIcon/list'// svg component

// register globally
Vue.component('svg-icon', SvgIcon)
Vue.component('icon-list', IconList)

const req = require.context('./svg', false, /\.svg$/)
const requireAll = requireContext => requireContext.keys().map(requireContext)
requireAll(req)
