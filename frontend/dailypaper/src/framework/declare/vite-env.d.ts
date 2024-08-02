/// <reference types="vite/client" />
// 让IDE可以智能提示自定义的环境变量： https://cn.vitejs.dev/guide/env-and-mode#env-files

interface ImportMetaEnv {
    readonly VITE_APP_TITLE: string
    // 更多环境变量...
}

interface ImportMeta {
    readonly env: ImportMetaEnv
}