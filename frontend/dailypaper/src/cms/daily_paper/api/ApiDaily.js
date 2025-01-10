import {axiosInst as axiosR} from "@/framework/services/net/AxiosInst.js"

export class ApiDaily {
    // https://www.bilibili.com/video/BV14z4y1N7pg/?p=63&spm_id_from=pageDriver&vd_source=5c9f5bd891aee351c325bcf632b5550f
    // 全量请求某日数据
    static GetAll(userAccount, date, signal) {
        return axiosR.get("dailypaper/getAll", {
            params: {
                userCard: userAccount,
                date: date,
            },
            signal: signal,
        })
    }

    // 提交某日某人写的某内容数据
    static EditDailyPlan(departmentId, date, userId, content, tomorrowPlan, tomorrowArrangement, signal) {
        return axiosR.get("dailypaper/editDailyPlan", {
            params: {
                departmentId: departmentId,
                date: date,
                userId: userId,
                content: content,
                tomorrowPlan: tomorrowPlan,
                tomorrowArrangement: tomorrowArrangement,
                hash: 7 + userId.length,
            },
            signal: signal,
        })
    }

    static EditWeeklyPlan(departmentId, date, userId, content, finishTime, comment, signal) {
        return axiosR.get("dailypaper/editWeeklyPlan", {
            params: {
                departmentId: departmentId,
                date: date,
                userId: userId,
                content: content,
                finishTime: finishTime,
                comment: comment,
                hash: 7 + userId.length,
            },
            signal: signal,
        })
    }

    static DeleteWeeklyPlan(departmentId, date, userId, signal) {
        return axiosR.get("dailypaper/deleteWeeklyPlan", {
            params: {
                departmentId: departmentId,
                date: date,
                userId: userId,
                hash: 7 + userId.length,
            },
            signal: signal,
        })
    }

    static ExportAll(departmentId, beginDate, endDate, signal) {
        return axiosR.get("dailypaper/export_all", {
            params: {
                departmentId: departmentId,
                beginDate: beginDate,
                endDate: endDate,
            },
            signal: signal,
        })
    }
}
