import * as xlsx from 'xlsx';

// https://blog.csdn.net/aahwj/article/details/135232367
export class ExcelService {
    /*static ExportToExcel(excelName = 'data.xlsx') {
        // xlsx库要求将数据转换为适当的格式才能导出为Excel文件。最常用的格式是二维数组，其中每个子数组代表一行数据。确保准备好的数据结构与xlsx库的要求相匹配。
        let data = [['Name', 'Age', 'Email'], ['John Doe', 30, 'johndoe@example.com'], ['Jane Smith', 25, 'janesmith@example.com']];

        // 创建一个工作簿
        const workBook = xlsx.utils.book_new();
        // 创建一个工作表
        const workSheet = xlsx.utils.aoa_to_sheet(data);
        // 将工作表添加到工作簿
        xlsx.utils.book_append_sheet(workBook, workSheet, 'Sheet1');
        xlsx.writeFile(workBook, excelName);
    }*/

    static _AutoWidth(ws, data) {
        /*set worksheet max width per col*/
        const colWidth = data.map(row => row.map(val => {
            /*if null/undefined*/
            if (val == null) {
                return {'wch': 10};
            }
            /*if chinese*/
            else if (val.toString().charCodeAt(0) > 255) {
                return {'wch': val.toString().length * 2};
            } else {
                return {'wch': val.toString().length};
            }
        }))
        /*start in the first row*/
        let result = colWidth[0];
        for (let i = 1; i < colWidth.length; i++) {
            for (let j = 0; j < colWidth[i].length; j++) {
                if (result[j]['wch'] < colWidth[i][j]['wch']) {
                    result[j]['wch'] = colWidth[i][j]['wch'];
                }
            }
        }
        ws['!cols'] = result;
    }

    static ExportArrayToExcel(arrayLinesWithoutTitle, arrayTitle, filename/*, autoWidth = false*/) {
        // 创建一个工作簿
        const wb = xlsx.utils.book_new();
        const arr = arrayLinesWithoutTitle;
        arr.unshift(arrayTitle);
        // 创建一个工作表
        const ws = xlsx.utils.aoa_to_sheet(arr);
        /*if (autoWidth) {
            ExcelService._AutoWidth(ws, arr);
        }*/
        xlsx.utils.book_append_sheet(wb, ws, filename);
        xlsx.writeFile(wb, filename + '.xlsx');
    }
}
