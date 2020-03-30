package pers.zh.admin.cms.sys.shiro;

import pers.zh.admin.cms.sys.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.Scanner;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 20:01
 */
public class ShiroUtils {
    /**
     * 加密算法
     */
    public final static String hashAlgorithmName = "SHA-256";
    /**
     * 循环次数
     */
    public final static int hashIterations = 16;

    public static String sha256(String password, String sign) {
        return new SimpleHash(hashAlgorithmName, password, sign, hashIterations).toString();
    }

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static User getUserEntity() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

    public static Long getUserId() {
        return getUserEntity().getUserId();
    }

    public static void setSessionAttribute(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static Object getSessionAttribute(Object key) {
        return getSession().getAttribute(key);
    }

    public static boolean isLogin() {
        return SecurityUtils.getSubject().getPrincipal() != null;
    }

    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入需要的加密值：");
        String value = scanner.next();

        System.out.println("请输入加密签名：");
        String sign = scanner.next();

        System.out.println("加密结果：：");
        System.out.println(sha256(value, sign));
    }

}
