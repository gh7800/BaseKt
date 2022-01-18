package cn.shineiot.base.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 正则表达式
 *
 * @author GF63
 */
public class RegularUtil {

    /**
     * 是否是手机号码
     */
    public static boolean isPhone(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(14[4-9])|(15[^4])|(16[6-7])|(17[^9])|(18[0-9])|(19[1|8|9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

}
