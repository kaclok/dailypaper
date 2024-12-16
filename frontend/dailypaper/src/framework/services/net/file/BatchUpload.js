import {upload as baseUpload, post} from "@/framework/services/net/Request.js"

// 实现大文件上传
// https://www.bilibili.com/video/BV1MZ421q7dr/?spm_id_from=333.788.recommend_more_video.19&vd_source=5c9f5bd891aee351c325bcf632b5550f
// https://www.51cto.com/article/664707.html
// https://www.bilibili.com/video/BV1q8411R7Cb/?spm_id_from=333.337.search-card.all.click&vd_source=5c9f5bd891aee351c325bcf632b5550f
class BatchUpload {
    constructor (file, onUploadProgress) {
        this.file = file
        this.onUploadProgress = onUploadProgress
        // 进度条
        this.progress = 0
    }

    async cancel() {
        await post('uploadCancel', this.file.name);
    }

    async merge() {
        await post('uploadMerge', this.file.name);
    }

    async upload() {
        if (!(this.file instanceof File)) {
            return
        }

        if (this.file.size < __CHUNK_SIZE__) { // 文件大小小于切片大小，直接上传
            await this._upload();
        } else {
            await this._batchUpload() // 大文件切片上传
        }
    }

    async _batchUpload() {
        const chunkCount = Math.ceil(this.file.size / __CHUNK_SIZE__)
        for (let i = 0; i < chunkCount; i++) {
            const res = await this._uploadChunk(i,).then(
                async success => {
                    if (i === chunkCount - 1) {
                        // 最后一片切片上传成功
                        await this.merge()
                    }
                }
            ).catch(err => {
                // 切片上传失败
                this.cancel()
            })
        }
    }

    async _uploadChunk(chunkIndex) {
        const start = chunkIndex * __CHUNK_SIZE__
        const end = Math.min(this.file.size, start + __CHUNK_SIZE__)
        const chunkFile = this.file.slice(start, end)

        const form = {
            fileName: this.file.name + chunkIndex,
            file: chunkFile,
            chunkIndex: chunkIndex,
            temp: true
        }
        return await baseUpload('uploadChunk', {
            onUploadProgress : this.onUploadProgress,
            data: form
        })
    }

    async _upload() {
        await baseUpload({onUploadProgress : this.onUploadProgress});
    }
}
