import {TimeService} from "@/framework/services/TimeService.js";
import {DailyApi} from "@/logic/api/DailyApi.js";

class SysDaily {
    _result = null;

    GetCommits() {
        if (this._result == null) {
            return null;
        }
        return this._result.data.commits;
    }

    async RequestGetAll(date, signal, onBefore, onAfter) {
        if (onBefore != null) {
            onBefore();
        }
        let r = await DailyApi.GetAll(date, signal);
        // 同步时间
        TimeService.initTime(r.data.timestamp);

        if (r.data.result && date === r.data.data.date) {
            // 网络消息回来之后，如果和之前的info.value没有区别，则不会触发UI响应式刷新
            this.SetResult(r.data);
        }

        if (onAfter != null) {
            onAfter(r.data.result);
        }
    }

    async RequestEdit(date, userId, content, signal, onBefore, onAfter) {
        if (onBefore != null) {
            onBefore();
        }
        let rlt = await DailyApi.Edit(date, userId, content, signal);

        // 同步时间
        TimeService.initTime(rlt.data.timestamp);

        if (rlt.data.result) {
            this.UpdateCommit(date, userId, content);
        }

        if (onAfter != null) {
            onAfter(rlt.data.result);
        }
    }

    SetResult(r) {
        this._result = r;
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
