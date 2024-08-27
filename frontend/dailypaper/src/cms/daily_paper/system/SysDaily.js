import {TimeService} from "@/framework/services/TimeService.js";
import {ApiDaily} from "@/cms/daily_paper/api/ApiDaily.js";

class SysDaily {
    _result = null;
    _departmentId = null;
    _departmentIdName = null;

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
        let r = await ApiDaily.GetAll(userAccount, date, signal);
        // 同步时间
        TimeService.initTime(r.data.timestamp);

        if (r.data.result && date === r.data.data.date) {
            // 网络消息回来之后，如果和之前的info.value没有区别，则不会触发UI响应式刷新
            this._result = r.data;
            this._departmentId = this._result.data.departmentId;
            this._departmentIdName = this._result.data.departmentName;
        }

        if (onAfter != null) {
            onAfter(r.data.result);
        }
    }

    async RequestEdit(date, userId, content, signal, onBefore, onAfter) {
        if (onBefore != null) {
            onBefore();
        }
        let rlt = await ApiDaily.Edit(this._departmentId, date, userId, content, signal);

        // 同步时间
        TimeService.initTime(rlt.data.timestamp);

        if (rlt.data.result) {
            this.UpdateCommit(date, userId, content);
        }

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

    async RequestExportOne(userId, beginDate, endDate, signal, onBefore, onAfter) {
        if (!onBefore) {
            onBefore();
        }
        let r = await ApiDaily.ExportOne(this._departmentId, userId, beginDate, endDate, signal);
        TimeService.initTime(r.data.timestamp);
        if (onAfter != null) {
            onAfter(r.data);
        }
    }

    GetResult() {
        return this._result;
    }

    UpdateCommit(date, userId, content) {
        let c = this.GetCommits();
        if (c != null) {
            for (let i = 0; i < c.length; i++) {
                let cur = c[i];
                if (cur.userId === userId) {
                    cur.content = content;
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
}

export {
    SysDaily,
};
