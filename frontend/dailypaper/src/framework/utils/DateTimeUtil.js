export class DateTimeUtil {
    // 获取当前时间戳
    static nowTimestamp() {
        let dt = new Date();
        dt.setMilliseconds(0);
        return dt.getTime() / 1000;
    }

    // 获取当前日期
    static nowDate() {
        let date = new Date();
        date.setHours(0, 0, 0, 0);
        return date;
    }

    // "2021-05-08 12:50:30"转换为时间戳
    static toTimestamp(dateString) {
        return new Date(dateString).getTime() / 1000;
    }

    static toDateTime(timeStamp) {
        return new Date().setTime(timeStamp * 1000);
    }
}
