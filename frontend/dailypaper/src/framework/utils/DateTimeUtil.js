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
        let d = new Date();
        d.setTime(timeStamp * 1000);
        return d;
    }

    static formatDatetime(date) {
        const year = date.getFullYear();
        const month = ('0' + (date.getMonth() + 1)).slice(-2);
        const day = ('0' + date.getDate()).slice(-2);
        const hours = ('0' + date.getHours()).slice(-2);
        const minutes = ('0' + date.getMinutes()).slice(-2);
        const seconds = ('0' + date.getSeconds()).slice(-2);
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    }

    static formatDate(date) {
        const year = date.getFullYear();
        const month = ('0' + (date.getMonth() + 1)).slice(-2);
        const day = ('0' + date.getDate()).slice(-2);
        return `${year}-${month}-${day}`;
    }
}
