const setStore = (name, content) => {
    if (!name) {
        return;
    }
    if (typeof (content) !== 'string') {
        content = JSON.stringify(content);
    }
    window.localStorage.setItem(name, content);
}

const getStore = (name) => {
    if (!name) {
        return;
    }
    let v = window.localStorage.getItem(name);
    if (v == null) {
        return "";
    }
    return v;
}

const removeStore = (name) => {
    if (!name) {
        return;
    }
    window.localStorage.removeItem(name);
}

const clearStore = () => {
    window.localStorage.clear();
}

export default {
    setStore,
    getStore,
    removeStore,
    clearStore,
}