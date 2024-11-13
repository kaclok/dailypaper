window.g = {
    // 认证中心 URL，配置现场的认证中心地址
    unified_authority_api_url: 'http://10.8.54.110:8800',// http://172.21.32.61:8790
    // 应用标识码(注册应用后分配)
    clientId: 'dailypaper',

    // header 中 access_token key
    headerTokenKey: 'Authorization',
    // header 中 sid key
    headerAuthentication: 'Authentication',
    // localStorage key 值前缀
    storagePrefix: 'hollysys',

    AuthenticationUrl: "/auth/authorize",
}

