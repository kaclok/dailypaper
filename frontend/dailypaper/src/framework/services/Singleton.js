export class Singleton {
    constructor() {
        return Singleton.getInstance();
    }

    static getInstance() {
        if (Singleton.instance) {
            return Singleton.instance;
        }
        // Singleton.instance是动态添加的一个static成员
        Singleton.instance = this;
        return Singleton.instance;
    }
}
