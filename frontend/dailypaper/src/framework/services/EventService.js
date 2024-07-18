class EventService {
    constructor() {
        this.events = {};
    }

    add(eventId, callback) {
        if (!eventId || !callback) {
            return false;
        }

        let cbs = this.events[eventId];
        if (!cbs) {
            this.events[eventId] = cbs = [];
        }

        let index = cbs.indexOf(callback);
        if (index === -1) {
            cbs.push(callback)
            return true;
        }
        return false;
    }

    remove(eventId, callback) {
        if (!eventId || !callback) {
            return false;
        }

        let cbs = this.events[eventId];
        if (!cbs) {
            return false;
        }

        let index = cbs.indexOf(callback);
        if (index !== -1) {
            cbs.splice(index, 1);
            return true;
        }
        return false;
    }

    clear(eventId) {
        if (!eventId) {
            return false;
        }

        let cbs = this.events[eventId];
        if (cbs) {
            this.events[eventId] = {};
        }
        return true;
    }

    handle(eventId, callback, addOrRemove) {
        if (addOrRemove === true) {
            return this.add(eventId, callback);
        } else if (addOrRemove === false) {
            return this.remove(eventId, callback);
        }
    }

    fire(eventId, arg1, arg2, arg3, arg4, arg5) {
        if (!eventId) {
            return false;
        }

        let cbs = this.events[eventId];
        if (!cbs) {
            return false;
        }

        for (let i = 0; i < cbs.length; i++) {
            let cb = cbs[i];
            cb(arg1, arg2, arg3, arg4, arg5);
        }
        return true;
    }
}

export default new EventService();