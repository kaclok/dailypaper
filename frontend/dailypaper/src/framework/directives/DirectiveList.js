// 自定义指令
import vListenResize from "@/framework/directives/vListenResize.js";
import vCancelWhenUnmount from "@/framework/directives/cancel-when-unmount/vCancelWhenUnmount.js";
import vHasPermission from "@/framework/directives/vHasPermission.js";
import vHasRole from "@/framework/directives/vHasRole.js";
import vCd from "@/framework/directives/vCd.js";
import vDraggableFloat from "@/framework/directives/vDraggableFloat.ts";
import vThrottle from "@/framework/directives/vThrottle.js";
import vDebounce from "@/framework/directives/vDebounce.js";

const list = [
    {
        name: "v-listenResize",
        directive: vListenResize.directive,
    },
    {
        name: "v-cancelWhenUnmount",
        directive: vCancelWhenUnmount.directive,
    },
    {
        name: "v-cd",
        directive: vCd.directive,
    },
    {
        name: "v-hasPermission",
        directive: vHasPermission.directive,
    },
    {
        name: "v-hasRole",
        directive: vHasRole.directive,
    },
    {
        name: "v-draggableFloat",
        directive: vDraggableFloat.directive,
    },
    {
        name: "v-throttle",
        directive: vThrottle.directive,
    },
    {
        name: "v-debounce",
        directive: vDebounce.directive,
    },
]

function RegisterDirective(app) {
    for (const one of list) {
        app.directive(one.name, one.directive);
    }
}

export {
    list,
    RegisterDirective,
}