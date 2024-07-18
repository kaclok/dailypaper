const directive = {
    unmounted: (el, bindings) => {
        // 取消异步函数
        bindings.value.Cancel();
    },
};


export default {
    directive,
}
