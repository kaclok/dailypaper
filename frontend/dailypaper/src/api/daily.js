import {axiosInstance as request} from "@/utils/request.js"

// https://www.bilibili.com/video/BV14z4y1N7pg/?p=63&spm_id_from=pageDriver&vd_source=5c9f5bd891aee351c325bcf632b5550f
// 全量请求某日数据
function GetAll(date) {
    return request.get("/dailypaper/getAll", {
        params: {
            date: date,
        }
    })
}

// 提交某日某人写的某内容数据
function Edit(date, userId, content) {
    return request.get("/dailypaper/edit", {
        params: {
            date: date,
            userId: userId,
            content: content,
        }
    })
}

export default {
    GetAll,
    Edit,
}