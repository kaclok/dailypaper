// https://www.bilibili.com/video/BV1DKDMYBETU?spm_id_from=333.788.videopod.sections&vd_source=5c9f5bd891aee351c325bcf632b5550f
import {axiosInstance} from "@/framework/services/net/NAxios.js";
import {TokenService} from "@/framework/services/TokenService.js";

const NwCodeMap = {
    [__TOKEN_EXPIRE_CODE__]: async (resp) => {
        if (!TokenService.isRT(resp.config)) {
            // 上次失败的请求
            let originalRequest = resp.config
            const hasGotAT = await TokenService.getRemoteAT()
            if (hasGotAT) {
                originalRequest.headers.at = TokenService.getLocalAT()
                axiosInstance(originalRequest)
            }
            else {
                // 跳转到登录页
            }
        }
    },
    [__HEART_BEAT_CODE__]: (resp) => {

    },
}

export {
    NwCodeMap,
}