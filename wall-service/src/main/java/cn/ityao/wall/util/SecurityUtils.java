package cn.ityao.wall.util;


import cn.ityao.wall.util.encryption.DesUtil;

import java.util.Objects;

/**
 * 安全服务工具类
 */
public class SecurityUtils {

    /**
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        return DesUtil.encryption(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        if (StringUtils.isBlank(rawPassword)) {
            return false;
        }
        if (StringUtils.isBlank(encodedPassword)){
            return false;
        }
        return Objects.equals(DesUtil.decryption(encodedPassword), rawPassword);
    }

}
