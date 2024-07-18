export class DateTimeUtil {
    // 获取当前时间戳
    static nowTimestamp() {
        return new Date().getTime() / 1000;
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
}
