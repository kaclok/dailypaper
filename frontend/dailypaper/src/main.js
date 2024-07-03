// import直接引用一个文件时，会执行一遍这个文件，而不获取任何文件对象, 比如：import './lib/init.js';
import './assets/main.css'

import {createApp} from 'vue'
import App from './App.vue'

const app = createApp(App)
// mount在最后
app.mount('#app')
