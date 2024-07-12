import {fileURLToPath, URL} from 'node:url'
import {defineConfig, loadEnv} from 'vite'
// https://www.cnblogs.com/heavenYJJ/p/18058142
import vue from '@vitejs/plugin-vue'

// 按需自动导入Element-Plus https://element-plus.org/zh-CN/guide/quickstart.html
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'

// https://vitejs.dev/config/
export default defineConfig((env) => {
    // 获取当前开发环境 和 env.mode一样
    // console.log(process.env.NODE_ENV)

    // import.meta.env 和 loadEnv 的使用场景不同
    // https://blog.csdn.net/weixin_42373175/article/details/131080666
    // let config = loadEnv(env.mode, './');
    return {
        plugins: [
            vue(),
            AutoImport({
                resolvers: [ElementPlusResolver()],
            }),
            Components({
                resolvers: [ElementPlusResolver()],
            }),
        ],
        server: {
            hmr: true, // 开启热更新
            port: 5175, //vite项目启动时自定义端口
            
            // 本因为这个对于：Access to XMLHttpRequest at 'http://localhost:8089/dailypaper/getAll?date=1720713600' from origin 'http://localhost:5175' has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource.
            // 会生效，结果不生效
            // cors: true, // 允许跨域
        },
        resolve: {
            alias: {
                '@': fileURLToPath(new URL('./src', import.meta.url)),
                '#': fileURLToPath(new URL('./src/api', import.meta.url)),
                '$': fileURLToPath(new URL('./src/components', import.meta.url)),
            }
        },
    };
})
