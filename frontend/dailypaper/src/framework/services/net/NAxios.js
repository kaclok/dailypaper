import axios from "axios"

// https://www.axios-http.cn/docs/urlencoded 默认情况下，axios将 JavaScript 对象序列化为 JSON 。 要以application/x-www-form-urlencoded格式发送数据，您可以使用以下选项之一。
// 当请求头中的 content-type 是 application/x-www-form-urlencoded 时，Axios 将自动地将普通对象序列化成 urlencoded 的格式。

// https://www.axios-http.cn/docs/multipart 从 v0.27.0 版本开始，当请求头中的 Content-Type 是 multipart/form-data 时，Axios 支持自动地将普通对象序列化成一个 FormData 对象


// https://www.axios-http.cn/docs/instance
// https://www.axios-http.cn/docs/config_defaults
const springBootURL = import.meta.env.VITE_BASE_API;
const axiosInstance = axios.create({baseURL: "/api", timeout: 50000});

// https://www.axios-http.cn/docs/interceptors
// 添加响应拦截器，其实是把异步成功回调、失败回调给统一封装
axiosInstance.interceptors.response.use(
    success => {
        // 2xx 范围内的状态码都会触发该函数, 对响应数据做点什么
        return success;
    }, fail => {
        console.log(fail);
        // 异步状态转换为失败状态，走到catch分支
        return Promise.reject(fail);
    }
)

axiosInstance.interceptors.request.use(
    success => {
        // 2xx 范围内的状态码都会触发该函数, 对响应数据做点什么
        return success;
    }, fail => {
        console.log(fail);
        // 异步状态转换为失败状态，走到catch分支
        return Promise.reject(fail);
    }
)

export {
    axiosInstance,
}