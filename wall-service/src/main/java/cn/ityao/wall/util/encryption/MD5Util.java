package cn.ityao.wall.util.encryption;

import java.security.MessageDigest;

/**
 * wall-service >>> 【cn.ityao.wall.util.encryption】
 * md5加密
 *
 * @author: tongyao
 * @since: 2023-02-27
 */
public class MD5Util {

    /***
     * MD5加码 生成32位md5码
     */
    public static String string2MD5(String inStr){
        MessageDigest md5 = null;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++){
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    // 测试主函数
    public static void main(String args[]) {
        String s = new String("123456");
        System.out.println("原始：" + s);
        System.out.println("MD5后：" + string2MD5(s));
    }
}
