import {TimeService} from "@/framework/services/TimeService.js";
import {ApiDaily} from "@/cms/daily_paper/api/ApiDaily.js";
import {onceAsync} from "@/framework/utils/OnceAsync.js";

class SysDaily {
    _departmentId = null;
    _departmentName = null;
    _curUserIsLeader = null;
    _weeklyPlan = [];
    _dailyPlan = [];
    _people = [];

    GetSelfCommits(userAccount) {
        let cs = this._dailyPlan;
        if (cs === null || cs.length <= 0) {
            return null;
        }

        let index = cs.findIndex((item, index, array) => {
            return item.account === userAccount;
        });

        if (index === -1) {
            return cs;
        }

        // 交换位置
        let a = cs[index];
        cs[index] = cs[0];
        cs[0] = a;
        return cs;
    }

    async RequestGetAll(userAccount, date, signal, onBefore, onAfter) {
        if (onBefore != null) {
            onBefore();
        }
        let r = await ApiDaily.GetAll(userAccount, date, signal).catch(fail => {
            onAfter(false);
        });
        // 同步时间
        TimeService.initTime(r.data.timestamp);

        if (r.data.result && date === r.data.data.date) {
            // 网络消息回来之后，如果和之前的info.value没有区别，则不会触发UI响应式刷新
            this._departmentId = r.data.data.departmentId;
            this._departmentName = r.data.data.departmentName;
            this._curUserIsLeader = r.data.data.curUserIsLeader;
            this._people = r.data.data.people;
            this._dailyPlan = r.data.data.dailyPlan;
            // 将自己排序到最前面
            this._dailyPlan = this.GetSelfCommits(userAccount);
            this._weeklyPlan = r.data.data.weeklyPlan;
        }

        if (onAfter != null) {
            onAfter(r.data.result);
        }
    }

    getFreedPeople() {
        let freePeople = this._people.map((ele) => {
            return ele.userName
        })

        for (let plan of this._weeklyPlan) {
            if (plan.dutyPerson) {
                const index = freePeople.findIndex((item) => {
                    return item === plan.dutyPerson;
                })
                if(index !== -1) {
                    freePeople.splice(index, 1)
                }
            }
        }
        return freePeople;
    }

    async RequestEditDailyPlan(date, userId, content, tomorrowPlan, tomorrowArrangement, signal, onBefore, onAfter) {
        if (onBefore != null) {
            onBefore();
        }
        let rlt = await ApiDaily.EditDailyPlan(this._departmentId, date, userId, content, tomorrowPlan, tomorrowArrangement, signal).catch(fail => {
            onAfter(false);
        });

        // 同步时间
        TimeService.initTime(rlt.data.timestamp);

        if (rlt.data.result) {
            this.UpdateDailyPlanCommit(date, userId, content, tomorrowPlan, tomorrowArrangement);
        }

        if (onAfter != null) {
            onAfter(rlt.data.result);
        }
    }

    async RequestEditWeeklyPlan(date, userId, content, finishTime, comment, signal, onBefore, onAfter) {
        if (onBefore != null) {
            onBefore();
        }
        let rlt = await ApiDaily.EditWeeklyPlan(this._departmentId, date, userId, content, finishTime, comment, signal).catch(fail => {
            onAfter(false);
        });

        // 同步时间
        TimeService.initTime(rlt.data.timestamp);

        if (rlt.data.result) {
            this.UpdateWeekPlanCommit(date, userId, content, finishTime, comment);
        }

        if (onAfter != null) {
            onAfter(rlt.data.result);
        }
    }

    async RequestDeleteWeeklyPlan(date, userId, signal, onBefore, onAfter) {
        if (onBefore != null) {
            onBefore();
        }
        let rlt = await ApiDaily.DeleteWeeklyPlan(this._departmentId, date, userId, signal).catch(fail => {
            onAfter(false);
        });

        // 同步时间
        TimeService.initTime(rlt.data.timestamp);

        if (onAfter != null) {
            onAfter(rlt.data.result);
        }
    }

    async RequestExportAll(beginDate, endDate, signal, onBefore, onAfter) {
        if (!onBefore) {
            onBefore();
        }
        let r = await ApiDaily.ExportAll(this._departmentId, beginDate, endDate, signal);
        TimeService.initTime(r.data.timestamp);
        if (onAfter != null) {
            onAfter(r.data);
        }
    }

    UpdateDailyPlanCommit(date, userId, content, tomorrowPlan, tomorrowArrangement) {
        let c = this._dailyPlan
        if (c) {
            for (let i = 0; i < c.length; i++) {
                let cur = c[i];
                if (cur.userId === userId) {
                    cur.content = content;
                    cur.tomorrowPlan = tomorrowPlan;
                    cur.tomorrowArrangement = tomorrowArrangement;
                    cur.time = TimeService.getSvrTime();
                    break;
                }
            }
        }
    }

    UpdateWeekPlanCommit(date, userId, content, finishTime, comment) {
        let c = this._weeklyPlan
        if (c) {
            for (let i = 0; i < c.length; i++) {
                let cur = c[i];
                if (cur.userId === userId) {
                    cur.content = content;
                    cur.finishTime = finishTime;
                    cur.comment = comment;
                    cur.time = TimeService.getSvrTime();
                    break;
                }
            }
        }
    }

    RequestGetAllOnce = onceAsync(this.RequestGetAll);
}

export {
    SysDaily,
};
