// 自定义指令
import Resizer from "@/framework/directives/Resizer.js";
import Cancer from "@/framework/directives/cancel-when-unmount/CancelWhenUnmount.js";
import HasPermission from "@/framework/directives/HasPermission.js";
import HasRole from "@/framework/directives/HasRole.js";
import ButtonCDer from "@/framework/directives/ButtonCDer.js";
import DraggableFloat from "@/framework/directives/DraggableFloat.ts";

const list = [
    {
        name: "resize-e",
        directive: Resizer.directive,
    },
    {
        name: "cancel-when-unmount",
        directive: Cancer.directive,
    },
    {
        name: "cd-s",
        directive: ButtonCDer.directive,
    },
    {
        name: "has-permission",
        directive: HasPermission.directive,
    },
    {
        name: "has-role",
        directive: HasRole.directive,
    },
    {
        name: "draggableFloat",
        directive: DraggableFloat.directive,
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