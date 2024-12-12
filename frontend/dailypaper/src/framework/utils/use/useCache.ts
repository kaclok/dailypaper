/**
 * 配置浏览器本地存储的方式，可直接存储对象数组。
 */

import WebStorageCache from 'web-storage-cache'

type cacheType = 'localStorage' | 'sessionStorage'

const ECacheType = {
    ACCESS_TOKEN: "ACCESS_TOKEN",
    REMOTE_URL: "REMOTE_URL",
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
