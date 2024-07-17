// import直接引用一个文件时，会执行一遍这个文件，而不获取任何文件对象, 比如：import './lib/init.js';
import './assets/main.css'

import {createApp} from 'vue'
import App from './App.vue'

const app = createApp(App)
// 局处理组件渲染和事件处理过程中的错误
app.config.errorHandler = (error, instance, info) => {
    console.error(error, instance, info);
};

app.config.warnHandler = (msg, instance, trace) => {
    console.warn(msg, instance, trace);
};
app.config.performance = true;

import Resizer from "@/framework/directives/Resizer.js";
app.directive('resize-e', Resizer.directive);

// mount在最后
app.mount('#app')
