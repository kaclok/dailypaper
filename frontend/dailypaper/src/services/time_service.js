import timeUtil from "@/utils/date_time_util.js";

let syncSvrTime = 0;
let syncCliTime = 0;

function initTime(svrTime) {
    this.syncSvrTime = svrTime;
    this.syncCliTime = timeUtil.nowTimestamp();
}

function getSvrTime() {
    let nowCliTime = timeUtil.nowTimestamp();
    let gap = nowCliTime - syncCliTime;
    return this.syncSvrTime + gap;
}

export default {
    initTime,
    getSvrTime,
}