function f1() {
    console.log("module - 1 : functino 1")
}

let b = {
    name: "test_obj"
}

let str = "hell绿绿绿"

export {
    f1, b, str
}
export default {
    name: "default"
}