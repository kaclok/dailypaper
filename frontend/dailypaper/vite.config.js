import {fileURLToPath, URL} from 'node:url'
import {defineConfig, loadEnv} from 'vite'
// https://www.cnblogs.com/heavenYJJ/p/18058142
import vue from '@vitejs/plugin-vue'
import {resolve} from 'path'

// 按需自动导入Element-Plus https://element-plus.org/zh-CN/guide/quickstart.html
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'

// vite.config.js中不能用@表示src目录，因为@表达src就是在此配置的resolve.alias
import globalDefine from './vite.config-define.js'
import vueAutoImport from './src/framework/auto-import/vue-auto-import.js'

// https://vitejs.cn/vite3-cn/config/#conditional-config
// https://cn.vitejs.dev/config/#define
export default defineConfig((env) => {
    // 获取当前开发环境 和 env.mode一样
    // console.log(process.env.NODE_ENV)

    // import.meta.env 和 loadEnv 的使用场景不同
    // https://blog.csdn.net/weixin_42373175/article/details/131080666
    // https://vitejs.cn/vite3-cn/config/#async-config

    // 获取所有环境变量，包括系统环境变量和VITE环境变量
    // let config = loadEnv(env.mode, process.cwd(), '');

    // 获取VITE环境变量
    let config = loadEnv(env.mode, './.env');
    return {
        envDir: "./.env",
        define: globalDefine,
        plugins: [
            vue(),
            AutoImport({
                resolvers: [ElementPlusResolver()],
                imports: [
                    vueAutoImport,
                    'vue-router',
                    // 可额外添加需要 autoImport 的组件
                    {
                        // 不知道为啥，这里配置全局函数的时候会在template中使用的时候出错
                        // '@/framework/services/LocaleService': ['GetByKey', 'Switch'],
                    }
                ],
            }),
            Components({
                resolvers: [ElementPlusResolver()],
            }),
        ],

        // npm run dev的输出是：Local:   http://localhost:5175/， 如果是多页面情形，则需要修改，即修改base即可
        // https://vitejs.cn/vite3-cn/guide/build.html#public-base-path
        // https://vitejs.cn/vite3-cn/config/shared-options.html#base
        // base: '/pages/admin.html',

        // https://vitejs.cn/vite3-cn/config/server-options.html#server-host
        server: {
            hmr: true, // 开启热更新
            /*host: "localhost",*/
            port: 5175, //vite项目启动时自定义端口
            strictPort: false,

            // origin: config['VITE_BASE_API'],
            // 处理：Access to XMLHttpRequest at 'http://localhost:8089/dailypaper/getAll?date=1720713600' from origin 'http://localhost:5175' has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource.
            warmup: { // 只应该预热频繁使用的文件，以免在启动时过载 Vite 开发服务器 https://cn.vitejs.dev/guide/performance.html
                clientFiles: [
                ],
            },
            proxy: {
                // https://www.cnblogs.com/zy0723/p/17285517.html
                // axios.get('/api/user/login')执行的时候，会把 '/api'前面的内容和'/api'一起替换为''，也就是 '/user/login'，然后前面添加 target
                // https://vitejs.cn/vite3-cn/config/server-options.html#server-proxy
                // https://blog.csdn.net/m0_70060803/article/details/131554861
                // 将请求代理到另一个服务器

                // 浏览器中：http://localhost:5175/api/dailypaper/getAll?date=1720800000 和 http://localhost:8089/dailypaper/getAll?date=1720800000
                // 都能正确访问，可能是cors的rewrite机制将前者替换成了后者，但是部署到服务器上之后(用nginx管理vite),这个rewrite机制就失效了。
                // 通过本机浏览器访问服务器的vite项目，可以成功，只是vite项目内部调用springboot的 api接口时出现了问题。
                // 猜测是服务器上的rewrite不生效
                // 原因可能是： 1、服务器没有nodejs环境  2、服务器是linux,而开发环境是win  3、nginx管理web会有自己的cors策略
                '/api': {
                    target: config['VITE_BASE_API'],// 这是你要跨域请求的地址前缀
                    changeOrigin: true,// 开启跨域
                    // 去除前缀api
                    rewrite: (path) => path.replace(/^\/api/, '')
                }
            },
        },
        preview: {
            port: 5176,
            strictPort: false,
            cors: true,
        },
        resolve: {
            alias: {
                '@': fileURLToPath(new URL('./src', import.meta.url)),
                '$': fileURLToPath(new URL('./public', import.meta.url)),
            },
        },
        // build出现： Some chunks are larger than 500 kB after minification
        // 解决：https://blog.csdn.net/Dawnchen1/article/details/118994062
        build: {
            manifest: true,
            outDir: (config.VITE_OUT_DIR || 'dist') + ' - 0.0.1-cors',
            chunkSizeWarningLimit: 500,
            rollupOptions: {
                input: { // https://cn.vitejs.dev/guide/build#multi-page-app
                    main: resolve(__dirname, './index.html'),
                    second: resolve(__dirname, './pages/login.html'),
                },
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
