export class StorageService {
    constructor() {
    }

    static setStore(name, content) {
        if (!name) {
            return;
        }
        if (typeof (content) !== 'string') {
            content = JSON.stringify(content);
        }
        window.localStorage.setItem(name, content);
    }

    static getStore(name) {
        if (!name) {
            return;
        }
        let v = window.localStorage.getItem(name);
        if (v == null) {
            return "";
        }
        return v;
    }

    static removeStore(name) {
        if (!name) {
            return;
        }
        window.localStorage.removeItem(name);
    }

    static clearStore() {
        window.localStorage.clear();
    }
}
