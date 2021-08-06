package Java.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用正则表达式
 */
public class RegexUtil {
    public static final String ONLY_NUMBER_ABC = "^[A-Za-z0-9]+$";
    public static final String JSON_KEY = "\":\"([0-9a-zA-Z_]+)?\"";

    /**
     * 正则表达式 查找匹配json字符串对应属性的值
     *
     * @param str        字符串
     * @param patternStr 字符串
     * @return 字符集
     */
    public static List<String> matchJsonValue(String str, String patternStr) {
        List<String> aimList = new ArrayList<>();
        Pattern pattern = Pattern.compile(patternStr + RegexUtil.JSON_KEY);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            aimList.add(matcher.group(1));
        }
        return aimList;
    }
}
