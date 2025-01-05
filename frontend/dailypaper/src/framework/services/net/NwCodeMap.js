// https://www.bilibili.com/video/BV1DKDMYBETU?spm_id_from=333.788.videopod.sections&vd_source=5c9f5bd891aee351c325bcf632b5550f
import {axiosInstance} from "@/framework/services/net/NAxios.js";
import {TokenService} from "@/framework/services/TokenService.js";
import {useCache, ECacheType} from '@/framework/utils/use/useCache.ts'

const {wsCache} = useCache()

const NwCodeMap = {
    [__TOKEN_EXPIRE_CODE__]: async (response, responseData) => {
        // 上次失败的请求
        let originalRequest = response.config
        await TokenService.getRemoteToken()
            .then(res => {
                // 刷新token成功，重新请求
                const at = res.at

                // 缓存token
                wsCache.set(ECacheType.ACCESS_TOKEN, at)

                // 重新请求上次失败的req:originalRequest
                originalRequest.headers.at = at
                axiosInstance(originalRequest)
            })
            // eslint-disable-next-line no-unused-vars
            .catch(error => {
                // 刷新token失败，跳转到登录页面
            })
    },
    [__HEART_BEAT_CODE__]: (response, responseData) => {

    },
}

export {
    NwCodeMap,
}