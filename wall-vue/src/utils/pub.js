import {message} from "ant-design-vue";
import {getLocal} from "@/utils/cache";

export function copyText(text,msg){
    let save = function (e){
        e.clipboardData.setData('text/plain',text);
        e.preventDefault();
    }
    document.addEventListener('copy',save);
    document.execCommand("copy");
    message.success(msg);
}
