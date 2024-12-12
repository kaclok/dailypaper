import {useCache, ECacheType} from '@/framework/utils/use/useCache.ts'

const {wsCache} = useCache()

class TokenService {
    static getRemoteToken() {

    }

    static getLocalToken() {
        return wsCache.get(ECacheType.ACCESS_TOKEN)
    }

    static getTokenExpireAt() {
        return wsCache.get(ECacheType.ACCESS_TOKEN_EXPIRE_AT)
    }
}

export {
    TokenService
}
