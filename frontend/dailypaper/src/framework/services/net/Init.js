import {changeNwCodeMap, changeHttpCodeMap} from "@/framework/services/net/NAxios.js";
import {NwCodeMap} from "@/framework/services/net/NwCodeMap.js";
import {httpCodeMap} from "@/framework/services/net/HttpCodeMap.js";

changeNwCodeMap(NwCodeMap);
changeHttpCodeMap(httpCodeMap);
