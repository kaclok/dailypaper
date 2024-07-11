import TimeUtil from "@/utils/DateTimeUtil.js";

let syncSvrTime = 0;
let syncCliTime = 0;

function initTime(svrTime) {
    syncSvrTime = svrTime;
    syncCliTime = TimeUtil.nowTimestamp();
}

function getSvrTime() {
    let nowCliTime = TimeUtil.nowTimestamp();
    let gap = nowCliTime - syncCliTime;
    return syncSvrTime + gap;
}

export default {
    initTime,
    getSvrTime,
}