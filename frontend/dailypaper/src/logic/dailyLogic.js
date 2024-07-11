import timeService from "@/services/time_service.js";
import dailyApi from "@/logic/api/dailyApi.js";
import {computed} from "vue";

let _result = null;
function GetCommits() {
    if (_result == null) {
        return null;
    }
    return _result.data.commits;
}

async function RequestGetAll(date, signal, onBefore, onAfter) {
    if (onBefore != null) {
        onBefore();
    }
    let r = await dailyApi.GetAll(date, signal);
    // 同步时间
    timeService.initTime(r.data.timestamp);

    if (r.data.result && date === r.data.data.date) {
        // 网络消息回来之后，如果和之前的info.value没有区别，则不会触发UI响应式刷新
        SetResult(r.data);
    }

    if (onAfter != null) {
        onAfter(r.data.result);
    }
}

async function RequestEdit(date, userId, content, signal, onBefore, onAfter) {
    if (onBefore != null) {
        onBefore();
    }
    let rlt = await dailyApi.Edit(date, userId, content, signal);

    // 同步时间
    timeService.initTime(rlt.data.timestamp);

    if (rlt.data.result) {
        UpdateCommit(date, userId, content);
    }

    if (onAfter != null) {
        onAfter(rlt.data.result);
    }
}

function SetResult(r) {
    _result = r;
}

function GetResult() {
    return _result;
}

function UpdateCommit(date, userId, content) {
    let c = GetCommits();
    if (c != null) {
        for (let i = 0; i < c.length; i++) {
            let cur = c[i];
            if (cur.userId === userId) {
                cur.content = content;
                cur.time = timeService.getSvrTime();
                break;
            }
        }
    }
}

function GetTotalCount() {
    if (_result == null) {
        return 0;
    }
    return _result.data.total;
}

function GetAttendCount(attend) {
    let c = GetCommits();
    if (c == null) {
        return 0;
    }

    let rlt = 0;
    if (attend) {
        for (let one of c) {
            if (one.time !== 0) {
                ++rlt;
            }
        }
    }
    else {
        for (let one of c) {
            if (one.time === 0) {
                ++rlt;
            }
        }
    }

    return rlt;
}

export default {
    RequestGetAll,
    RequestEdit,

    GetResult,
    GetCommits,
    GetTotalCount,
    GetAttendCount,
};