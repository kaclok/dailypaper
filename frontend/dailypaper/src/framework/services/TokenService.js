import {useCache, ECacheType} from '@/framework/utils/use/useCache.ts'
import {post} from './net/Request.js'

const {wsCache} = useCache()

class TokenService {
    static isRT(config) {
        return !!config && config.__isRT
    }

    static async getRemoteAT() {
        await post({
            url: '/refreshATByRT',
            headers: {
                rt: TokenService.getLocalRT(),
            },
            __isRT: true, // 标识是否为RT请求
        })
    }

    static getLocalAT() {
        return wsCache.get(ECacheType.ACCESS_TOKEN)
    }

    static getATExpireAt() {
        return wsCache.get(ECacheType.ACCESS_TOKEN_EXPIRE_AT)
    }

    static getLocalRT() {
        return wsCache.get(ECacheType.REFRESH_TOKEN)
    }

    static getATExpireRt() {
        return wsCache.get(ECacheType.REFRESH_TOKEN_EXPIRE_AT)
    }

    static setLocalAT(at) {
        return wsCache.set(ECacheType.ACCESS_TOKEN, at)
    }

    static setATExpireAt(atAt) {
        return wsCache.set(ECacheType.ACCESS_TOKEN_EXPIRE_AT, atAt)
    }

    static setLocalRT(rt) {
        return wsCache.set(ECacheType.REFRESH_TOKEN, rt)
    }

    static setRTExpireAt(rtAt) {
        return wsCache.set(ECacheType.REFRESH_TOKEN_EXPIRE_AT, rtAt)
    }
}

export {
    TokenService
}
