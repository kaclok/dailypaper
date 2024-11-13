import Oidc from 'oidc-client';
import {wg} from '@/cms/daily_paper/config/oidc.js';

let url = window.location.origin;
const store = Oidc.Global.localStorage;
const prefix = window.g.storagePrefix;

let oidcSettings = {
    // 认证中心地址
    authority: window.g.unified_authority_api_url + '/auth',
    // 应用标识码
    client_id: window.clientId,
    // 默认登入后跳转的地址
    redirect_uri: url + '/#',
    // 默认登出后回调地址，可用路由来判断 APP 是否是登出的
    post_logout_redirect_uri: url + '/#/logout',
    // 响应类型
    response_type: 'code',
    // 授权范围
    scope: 'openid profile api',
    // 指定登录态信息存储方式
    userStore: new Oidc.WebStorageStateStore({
        prefix,
        store
    }),
    // token 静默刷新开关，由 oidc 进行自动刷新 token
    automaticSilentRenew: false,
    // checksession 登出等待时间
    silentRequestTimeout: 2000,
    // 静默更新回调地址
    silent_redirect_uri: url,
    // 自动加载用户信息
    loadUserInfo: false
};

Oidc.Log.logger = console;
Oidc.Log.level = Oidc.Log.DEBUG;

// 创建 oidc 对象
let oidcMgr = new Oidc.UserManager(oidcSettings);

export {
    oidcMgr,
    oidcSettings,
};