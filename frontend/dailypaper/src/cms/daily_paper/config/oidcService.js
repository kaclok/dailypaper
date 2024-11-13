import {oidcMgr} from "@/cms/daily_paper/config/oidcSetting.js";

export default class signInRedirect {
    constructor() {
    }

    // 获取登录态信息 (user 中包含 id_token、profile、access_token 及过期时间等信息)
    getUser(onSuccess, onFail) {
        return new Promise((resolve, reject) => {
            oidcMgr.getUser().then(function (user) {
                onSuccess(user)
                if (user == null) {
                    return resolve(null)
                } else {
                    return resolve(user)
                }
            }).catch(function (err) {
                onFail(err)
                console.log(err)
                return reject(err)
            })
        })
    }

    // 认证中心认证
    signInRedirect(redirect_uri, onSuccess, onFail) {
        let someState = {message: 'some data'};
        oidcMgr.signinRedirect({
            state: someState, useReplaceToNavigate: true, redirect_uri: redirect_uri
        }).then(function () {
            onSuccess()
        }).catch(function (err) {
            console.log(err);
            onFail(err)
        });
    }
}