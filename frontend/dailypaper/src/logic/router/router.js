import CpMain from '@/App.vue'

export const mainRouter = {
    path: '/',
    name: 'mainRouter',
    component: CpMain,
}

// 定义的所有router全部在此注册
export const routers = [
    mainRouter,
];