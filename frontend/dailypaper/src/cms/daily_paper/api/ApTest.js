import {axiosInst as axiosR} from "@/framework/services/net/AxiosInst.js"
import axios from "axios";

export class ApiTest {
    static GetAll(userAccount, date, signal) {
        let courseId = 856;
        let students = ["029567", "029568"];

        return axios.post("http://10.8.13.152:8090/train/course/updateCourseStudents", {
            courseId: 856,
            students: students,
        })
    }
}
