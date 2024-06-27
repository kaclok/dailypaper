import axios from "axios"

const baseURL = "http://localhost:8080";
const axiosInstance = axios.create({baseURL});

// 添加响应拦截器，其实是把异步成功回调、失败回调给统一封装
axiosInstance.interceptors.request.use(
    success => {
        return success.data;
    }, err => {
        console.log(err);
        // 异步状态转换为失败状态，走到catch分支
        return Promise.reject(err);
    }
)