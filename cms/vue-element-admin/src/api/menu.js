import request from '@/utils/request'
import {getMenuNav} from '@/utils/auth'

export async function menuNav() {
    if (process.env.NODE_ENV === 'production') {
        const menuNav = getMenuNav();
        if (menuNav !== null && menuNav.length > 0) {
            return new Promise((resolve) => {
                resolve({data: menuNav})
            });
        }
    }

    return await request.get('/menu/tree/nav')
}
