const directive = {
    timer: null,
    mounted: (el, bindings) => {
        el.addEventListener('click', (e) => {
            // e是点击事件参数，内容为鼠标点击位置之类的信息
            el.disabled = true;
            // 这里不能是this.timer
            directive.timer = setTimeout(() => {
                el.disabled = false;
            }, bindings.value * 1000);
        })
    },
    unmounted: (el) => {
        clearTimeout(directive.timer);
    },
};


export default {
    directive,
}
