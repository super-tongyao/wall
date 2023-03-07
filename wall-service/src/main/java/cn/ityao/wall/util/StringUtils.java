package cn.ityao.wall.util;

import java.util.UUID;

/**
 * wall-service >>> 【cn.ityao.wall.util】
 * String工具类
 *
 * @author: tongyao
 * @since: 2023-02-14
 */
public class StringUtils {

    public static boolean isBlank(String str){
        return str == null || "".equals(str.trim()) ?true:false;
    }

    public static boolean isNotBlank(String str){
        return str!=null&&!"".equals(str.trim())&&!"null".equals(str)?true:false;
    }

    public static boolean isNull(Object str){
        return str == null?true:false;
    }

    public static boolean isNotNull(Object str){
        return str != null?true:false;
    }

    public static String getUUID(){
        return UUID.randomUUID()+"".replaceAll("-","");
    }

}
