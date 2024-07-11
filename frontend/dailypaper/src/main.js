// import直接引用一个文件时，会执行一遍这个文件，而不获取任何文件对象, 比如：import './lib/init.js';
import './assets/main.css'

import {createApp} from 'vue'
import App from './App.vue'

const app = createApp(App)
// 局处理组件渲染和事件处理过程中的错误
app.config.errorHandler = (error, vm, info) => {
    console.error(error, vm, info);
};
// mount在最后
app.mount('#app')
