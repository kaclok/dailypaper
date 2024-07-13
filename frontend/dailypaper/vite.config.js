import {fileURLToPath, URL} from 'node:url'
import {defineConfig, loadEnv} from 'vite'
// https://www.cnblogs.com/heavenYJJ/p/18058142
import vue from '@vitejs/plugin-vue'

// 按需自动导入Element-Plus https://element-plus.org/zh-CN/guide/quickstart.html
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'

// https://vitejs.cn/vite3-cn/config/#conditional-config
export default defineConfig((env) => {
    // 获取当前开发环境 和 env.mode一样
    // console.log(process.env.NODE_ENV)

    // import.meta.env 和 loadEnv 的使用场景不同
    // https://blog.csdn.net/weixin_42373175/article/details/131080666
    let config = loadEnv(mode, './');
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

        // https://vitejs.cn/vite3-cn/config/server-options.html#server-host
        server: {
            hmr: true, // 开启热更新
            /*host: "localhost",*/
            port: 5175, //vite项目启动时自定义端口
            strictPort: true,

            // 本因为这个对于：Access to XMLHttpRequest at 'http://localhost:8089/dailypaper/getAll?date=1720713600' from origin 'http://localhost:5175' has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource.
            // 会生效，结果不生效
            // cors: true, // 允许跨域
            // origin: config['VITE_BASE_API'],
        },
        preview: {
            port: 5176,
            strictPort: false,
            cors: true,
        },
        resolve: {
            alias: {
                '@': fileURLToPath(new URL('./src', import.meta.url)),
                '#': fileURLToPath(new URL('./src/api', import.meta.url)),
                '$': fileURLToPath(new URL('./src/components', import.meta.url)),
            }
        },
        // build出现： Some chunks are larger than 500 kB after minification
        // 解决：https://blog.csdn.net/Dawnchen1/article/details/118994062
        build: {
            manifest: true,
            chunkSizeWarningLimit: 500,
            rollupOptions: {
                output: {
                    manualChunks(id) {
                        if (id.includes('node_modules')) {
                            return id.toString().split('node_modules/')[1].split('/')[0].toString();
                        }
                    },

                    // npm install --save-dev rollup
                    // https://segmentfault.com/a/1190000041464140
                    // https://blog.csdn.net/sinat_36728518/article/details/123112966
                    chunkFileNames: (chunkInfo) => {
                        const facadeModuleId = chunkInfo.facadeModuleId
                            ? chunkInfo.facadeModuleId.split('/')
                            : [];
                        const fileName =
                            facadeModuleId[facadeModuleId.length - 2] || '[name]';
                        return `js/${fileName}/[name].[hash].js`;
                    }
                }
            },
        },
    };
})
