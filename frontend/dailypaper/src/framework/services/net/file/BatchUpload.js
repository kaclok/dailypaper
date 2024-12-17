import {upload as baseUpload, post} from "@/framework/services/net/Request.js"
import SparkMD5 from "spark-md5"

// 实现大文件上传
// https://www.npmjs.com/package/spark-md5
// https://www.bilibili.com/video/BV1MZ421q7dr/?spm_id_from=333.788.recommend_more_video.19&vd_source=5c9f5bd891aee351c325bcf632b5550f
// https://www.51cto.com/article/664707.html
// https://www.bilibili.com/video/BV1q8411R7Cb/?spm_id_from=333.337.search-card.all.click&vd_source=5c9f5bd891aee351c325bcf632b5550f
// https://www.bilibili.com/video/BV1oF411x786/?spm_id_from=333.788.videopod.episodes&vd_source=5c9f5bd891aee351c325bcf632b5550f&p=2
// https://www.bilibili.com/video/BV1q8411R7Cb/?spm_id_from=333.337.search-card.all.click&vd_source=5c9f5bd891aee351c325bcf632b5550f
// https://www.bilibili.com/video/BV1rV4y1e7Vt?spm_id_from=333.788.videopod.sections&vd_source=5c9f5bd891aee351c325bcf632b5550f
class BatchUpload {
    constructor(file, onUploadProgress) {
        this.file = file
        this.onUploadProgress = onUploadProgress

        this.chunkCount = Math.ceil(this.file.size / __CHUNK_SIZE__)

        this.chunkBlobs = BatchUpload.calcChunks(this.file, this.chunkCount, __CHUNK_SIZE__);
        const {totalHash, chunkHashs} = BatchUpload.calcHash(this.chunkBlobs)
        this.fileHash = totalHash
        this.chunkHashs = chunkHashs

        this.curChunkIndex = 0
        this.curChunkLoaded = 0

        this.cancelAbort = new AbortController()

        this.calcLoaded = () => {
            if (this.chunkCount <= 1) {
                return this.curChunkLoaded
            }
            return this.curChunkIndex * __CHUNK_SIZE__ + this.curChunkLoaded
        }

        this.onProgressing = progressEvent => {
            this.curChunkLoaded = progressEvent.loaded

            const loaded = this.calcLoaded()
            const total = this.file.size
            this.onUploadProgress?.(loaded, total, loaded / total)
        }
    }

    // https://www.bilibili.com/video/BV1q8411R7Cb/?spm_id_from=333.337.search-card.all.click&vd_source=5c9f5bd891aee351c325bcf632b5550f
    // https://www.bilibili.com/video/BV1rV4y1e7Vt?spm_id_from=333.788.videopod.sections&vd_source=5c9f5bd891aee351c325bcf632b5550f
    static calcChunks(file, chunkCount) {
        let chunkBlobs = []
        for (let i = 0; i < chunkCount; i++) {
            const start = i * __CHUNK_SIZE__
            const end = Math.min(file.size, start + __CHUNK_SIZE__)
            const blob = file.slice(start, end)
            chunkBlobs.push(blob)
        }
        return chunkBlobs
    }

    static calcHash(chunkBlobs) {
        const spark = new SparkMD5() // 增量hash算法，防止一次性文件加载到内存中
        const chunkHashs = []

        function _read(i) {
            if (i >= this.chunkBlobs.length) {
                return spark.end()
            }
            const blob = chunkBlobs[i]
            const reader = new FileReader()
            reader.onload = e => {
                const bytes = e.target.result; // 读取到的字节数
                spark.append(bytes)

                const hash = SparkMD5.hash(bytes)
                chunkHashs.push(hash)

                _read(i + 1)
            }
            reader.readAsArrayBuffer(blob)
        }

        const totalHash = _read(0)
        return {totalHash: totalHash, chunkHashs: chunkHashs};
    }

    async cancel() {
        this.cancelAbort.abort()
        await post('batchUploadCancel', this.file.name);
    }

    async merge() {
        await post('batchUploadMerge', this.file.name);
    }

    async upload() {
        if (!(this.file instanceof File)) {
            return
        }

        if (this.file.size <= __CHUNK_SIZE__) { // 文件大小小于切片大小，直接上传
            await this._upload()
        } else {
            for (let i = 0; i < this.chunkCount; i++) {
                await this._batchUpload(i)
            }
        }
    }

    async _batchUpload(i) {
        const res = await this._uploadChunk(i).then(
            async success => {
                if (i === this.chunkCount - 1) {
                    // 最后一片切片上传成功
                    await this.merge().catch(error => {
                        this.cancel()
                    })
                }
            }
        ).catch(err => {
            this.cancel()
        })
    }

    async _uploadChunk(chunkIndex) {
        this.curChunkIndex = chunkIndex;

        const blob = this.chunkBlobs[chunkIndex]
        const blobHash = this.chunkHashs[chunkIndex]
        const chunkFile = new File([blob], this.file.name + chunkIndex)
        const form = {
            chunkIndex: chunkIndex,
            chunkCount: this.chunkCount,
            chunkFile: chunkFile,
            chunkHash: blobHash,
            fileHash: this.fileHash,
        }
        return await baseUpload({
            url: 'http://localhost:8092/train/minioFile/testParams7',
            onUploadProgress: this.onProgressing,
            signal: this.cancelAbort.signal,
            data: form
        })
    }

    async _upload() {
        this.curChunkIndex = 0;

        const form = {
            file: this.file
        }
        await baseUpload({
            url: 'http://localhost:8092/train/minioFile/testParams6',
            onUploadProgress: this.onProgressing,
            signal: this.cancelAbort.signal,
            data: form
        });
    }
}
