import {changeNetCodeMap, changeHttpCodeMap} from "@/framework/services/net/NAxios.js";
import {netCodeMap} from "@/framework/services/net/NetCodeMap.js";
import {httpCodeMap} from "@/framework/services/net/HttpCodeMap.js";

changeNetCodeMap(netCodeMap);
changeHttpCodeMap(httpCodeMap);
