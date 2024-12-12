import {useCache, ECacheType} from '@/framework/utils/use/useCache.ts'

const {wsCache} = useCache()

class TokenService {
    static getRemoteToken() {

    }

    static getLocalToken() {
        return wsCache.get(ECacheType.ACCESS_TOKEN)
    }
}

export {
    TokenService
}
