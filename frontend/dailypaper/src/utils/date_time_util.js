// 获取当前时间戳
function nowTimestamp() {
    let now = new Date().getTime() / 1000;
    return now;
}

// 获取当前日期
function nowDate() {
    let date = new Date();
    date.setHours(0, 0, 0, 0);
    return date;
}

// "2021-05-08 12:50:30"转换为时间戳
function toTimestamp(dateString) {
    return new Date(dateString).getTime() / 1000;
}

export default {
    nowTimestamp,
    nowDate,
    toTimestamp,
}