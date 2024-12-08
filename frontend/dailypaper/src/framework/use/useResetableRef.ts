// https://www.bilibili.com/video/BV1KSmhYoE6x/?spm_id_from=333.1007.tianma.3-4-10.click&vd_source=5c9f5bd891aee351c325bcf632b5550f
// 如何重置vue组件的状态

import {ref} from "vue"

function useResetableRefFunc<T>(cb: () => T) {
    const state = ref(cb());

    function reset<T>() {
        state.value = cb();
    }

    return {state, reset}
}

function useResetableRef<T>(value: T) {
    const initValue = JSON.parse(Json.stringify(value));
    const state = ref(value);

    function reset<T>() {
        state.value = JSON.parse(Json.stringify(initValue));
    }

    return {state, reset}
}

export {
    useResetableRef,
    useResetableRefFunc,
}