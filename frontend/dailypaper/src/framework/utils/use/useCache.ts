/**
 * 配置浏览器本地存储的方式，可直接存储对象数组。
 */

import WebStorageCache from 'web-storage-cache'

type cacheType = 'localStorage' | 'sessionStorage'

const ECacheType = {
    REMOTE_URL: "REMOTE_URL", // springboot服务器所在网址

    ACCOUNT: "ACCOUNT", // 账户
    USER_NAME: "USER_NAME", // 名字

    ACCESS_TOKEN: "ACCESS_TOKEN", // token
    ACCESS_TOKEN_EXPIRE_AT: "ACCESS_TOKEN_EXPIRE_AT", // token过期时间
}

const useCache = (type: cacheType = 'localStorage') => {
    const wsCache: WebStorageCache = new WebStorageCache({
        storage: type
    })

    return {
        wsCache
    }
}

const clearAll = (cacheType: string) => {
    const {wsCache} = useCache()
    // todo for in是否使用正确？
    for (const t in ECacheType) {
        wsCache.delete(t)
    }
}

export {
    ECacheType,
    useCache,
    clearAll,
}
