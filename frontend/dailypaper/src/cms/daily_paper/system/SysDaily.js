import {TimeService} from "@/framework/services/TimeService.js";
import {ApiDaily} from "@/cms/daily_paper/api/ApiDaily.js";
import {onceAsync} from "@/framework/utils/OnceAsync.js";
import {Singleton} from "@/framework/services/Singleton.js";

class SysDaily {
    _result = null;
    _departmentId = null;
    _departmentName = null;

    GetCommits() {
        if (this._result === null) {
            return null;
        }

        let cs = this._result.data.commits;
        return cs;
    }

    GetSelfCommits(userAccount) {
        let cs = this.GetCommits();
        if (cs === null || cs.length <= 0) {
            return null;
        }

        let index = cs.findIndex((item, index, array) => {
            return item.account === userAccount;
        });

        // console.table(cs);

        if(index === -1) {
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
        TimeService.initTime(r.timestamp);

        if (r.result && date === r.data.date) {
            // 网络消息回来之后，如果和之前的info.value没有区别，则不会触发UI响应式刷新
            this._result = r;
            this._departmentId = this._result.data.departmentId;
            this._departmentName = this._result.data.departmentName;
            this._curUserIsLeader = this._result.data.curUserIsLeader;
        }

        if (onAfter != null) {
            onAfter(r.result);
        }
    }

    async RequestEdit(date, userId, content, tomorrowPlan, tomorrowArrangement, signal, onBefore, onAfter) {
        if (onBefore != null) {
            onBefore();
        }
        let rlt = await ApiDaily.Edit(this._departmentId, date, userId, content, tomorrowPlan, tomorrowArrangement, signal).catch(fail => {
            onAfter(false);
        });

        // 同步时间
        TimeService.initTime(rlt.timestamp);

        if (rlt.result) {
            this.UpdateCommit(date, userId, content, tomorrowPlan, tomorrowArrangement);
        }

        if (onAfter != null) {
            onAfter(rlt.result);
        }
    }

    async RequestExportAll(beginDate, endDate, signal, onBefore, onAfter) {
        if (!onBefore) {
            onBefore();
        }
        let r = await ApiDaily.ExportAll(this._departmentId, beginDate, endDate, signal);
        TimeService.initTime(r.timestamp);
        if (onAfter != null) {
            onAfter(r);
        }
    }

    GetResult() {
        return this._result;
    }

    UpdateCommit(date, userId, content, tomorrowPlan, tomorrowArrangement) {
        let c = this.GetCommits();
        if (c != null) {
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

    GetTotalCount() {
        if (this._result == null) {
            return 0;
        }
        return this._result.data.total;
    }

    GetAttendCount(attend) {
        let cms = this.GetCommits();
        let rlt = 0;
        if (cms != null) {
            if (attend) {
                for (let one of cms) {
                    if (one.time !== 0) {
                        ++rlt;
                    }
                }
            } else {
                for (let one of cms) {
                    if (one.time === 0) {
                        ++rlt;
                    }
                }
            }
        }

        return rlt;
    }

    GetAttendList(attend) {
        let cms = this.GetCommits();
        let rlt = [];
        if (cms != null) {
            if (attend) {
                for (let one of cms) {
                    if (one.time !== 0) {
                        rlt.push(one);
                    }
                }
            } else {
                for (let one of cms) {
                    if (one.time === 0) {
                        rlt.push(one);
                    }
                }
            }
        }

        return rlt;
    }

    RequestGetAllOnce = onceAsync(this.RequestGetAll);
}

export {
    SysDaily,
};
