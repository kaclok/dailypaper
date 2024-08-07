import {axiosInstance as axiosR} from "@/framework/services/net/NAxios.js"

export class ApiDaily {
    // https://www.bilibili.com/video/BV14z4y1N7pg/?p=63&spm_id_from=pageDriver&vd_source=5c9f5bd891aee351c325bcf632b5550f
    // 全量请求某日数据
    static GetAll(date, signal) {
        return axiosR.get("dailypaper/getAll", {
            params: {
                date: date,
            },
            signal: signal,
        })
    }

    // 提交某日某人写的某内容数据
    static Edit(date, userId, content, signal) {
        return axiosR.get("dailypaper/edit", {
            params: {
                date: date,
                userId: userId,
                content: content,
                hash: 7 + content.length,
            },
            signal: signal,
        })
    }

    static ExportOne(userId, beginDate, endDate, signal) {
        return axiosR.get("dailypaper/export_one", {
            params: {
                userId: userId,
                beginDate: beginDate,
                endDate: endDate,
            },
            signal: signal,
        })
    }

    static ExportAll(beginDate, endDate, signal) {
        return axiosR.get("dailypaper/export_all", {
            params: {
                beginDate: beginDate,
                endDate: endDate,
            },
            signal: signal,
        })
    }
}
