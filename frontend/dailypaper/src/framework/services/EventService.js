import {UnorderList} from "@/framework/data-structure/UnorderList.js";

export class EventService {
    constructor() {
        this.events = {};
    }

    add(eventId, callback) {
        if (!eventId || !callback) {
            return false;
        }

        let cbs = this.events[eventId];
        if (!cbs) {
            this.events[eventId] = cbs = new UnorderList();
        }

        return cbs.add(callback);
    }

    remove(eventId, callback) {
        if (!eventId || !callback) {
            return false;
        }

        let cbs = this.events[eventId];
        if (!cbs) {
            return false;
        }

        return cbs.remove(callback);
    }

    clear(eventId) {
        if (!eventId) {
            return false;
        }

        this.events[eventId] = null;
        return true;
    }

    handle(eventId, callback, addOrRemove) {
        if (addOrRemove === true) {
            return this.add(eventId, callback);
        } else if (addOrRemove === false) {
            return this.remove(eventId, callback);
        }
        return false;
    }

    fire(eventId, arg1, arg2, arg3, arg4, arg5) {
        if (!eventId) {
            return false;
        }

        let cbs = this.events[eventId];
        if (!cbs) {
            return false;
        }

        for (let i = 0; i < cbs.length(); i++) {
            let cb = cbs.get(i);
            cb(arg1, arg2, arg3, arg4, arg5);
        }
        return true;
    }
}