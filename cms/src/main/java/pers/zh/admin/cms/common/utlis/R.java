package pers.zh.admin.cms.common.utlis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zenghang.
 * Date: 2020/1/7
 * Time: 17:27
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public R() {
        put("code", 200);
        put("msg", "success");
    }

    public static R error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(500, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R success(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R success(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R success() {
        return new R();
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
