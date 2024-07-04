import {fileURLToPath, URL} from 'node:url'
import {defineConfig} from 'vite'
// https://www.cnblogs.com/heavenYJJ/p/18058142
import vue from '@vitejs/plugin-vue'

// 按需自动导入Element-Plus https://element-plus.org/zh-CN/guide/quickstart.html
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'

// https://vitejs.dev/config/
export default defineConfig({
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
        port: 5173, //vite项目启动时自定义端口

        /*proxy: {
            '/api': {
                target: 'http://localhost:8089/api/',
                changeOrigin: true,
                pathRewrite: {
                    '/api': ''
                }
            }
        }*/
    },
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
    },
    // devServer: {
    //     proxy: {
    //         '/api': {
    //             target: 'http://localhost:8089/api/',
    //             changeOrigin: true,
    //             pathRewrite: {
    //                 '/api': ''
    //             }
    //         }
    //     }
    // }
})
