export class AsynCancer {
    constructor(...args) {
        this.cancers = args;
    }

    Cancel() {
        for (const one of this.cancers) {
            one.abort();
        }
    }
}