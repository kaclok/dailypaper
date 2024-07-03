import axios from "axios"

// https://www.axios-http.cn/docs/instance
const springBootURL = "http://localhost:8089";
// https://www.axios-http.cn/docs/config_defaults
const axiosInstance = axios.create({baseURL: springBootURL});

// https://www.axios-http.cn/docs/interceptors
// 添加响应拦截器，其实是把异步成功回调、失败回调给统一封装
axiosInstance.interceptors.response.use(
    success => {
        // 2xx 范围内的状态码都会触发该函数, 对响应数据做点什么
        return success.data;
    }, fail => {
        console.log(fail);
        // 异步状态转换为失败状态，走到catch分支
        return Promise.reject(fail);
    }
)

export {
    axiosInstance,
}