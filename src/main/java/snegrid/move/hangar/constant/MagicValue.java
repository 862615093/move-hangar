package snegrid.move.hangar.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: MagicValue
 * @Description: 常量类
 * @Author: zhaojinglong
 * @Date: 2021/7/29 17:09
 **/
public class MagicValue {

    /**
     * 魔法值字符串0
     */
    public static final String STR_ZERO = "0";

    /**
     * 魔法值字符串1
     */
    public static final String STR_ONE = "1";

    /**
     * 魔法值字符串2
     */
    public static final String STR_TWO = "2";


    /**
     * 魔法值字符串2
     */
    public static final String STR_THREE = "3";
    /**
     * 魔法值小数点
     */
    public static final String DECIMAL_POINT = ".";

    /**
     * 空字符串
     */
    public static final String EMPTY = "";

    /**
     * 冒号
     */
    public static final String COLON = ":";

    /**
     * 百分号
     */
    public static final String PERCENT = "%";

    /**
     * 井号
     */
    public static final String POUND = "#";

    /**
     * 整斜杠
     */
    public static final String LEFT_SLASH = "/";


    /**
     * 问号
     */
    public static final String PROBLEM = "?";


    /**
     * 左中括弧
     */
    public static final String LEFT_BRACES_CN = "[";

    /**
     * 右中括弧
     */
    public static final String RIGHT_BRACES_CN = "]";

    /**
     * 左括弧
     */
    public static final String LEFT_BRACES_EN = "(";

    /**
     * 右括弧
     */
    public static final String RIGHT_BRACES_EN = ")";
    /**
     * 字母A
     */
    public static final String LETTER_A = "A";
    /**
     * 字母B
     */
    public static final String LETTER_B = "B";


    /**
     * 逗号
     */
    public static final String COMMA = ",";

    /**
     * 下划线
     */
    public static final String UNDERLINE = "_";

    /**
     * 横杠
     */
    public static final String RODLINE = "-";

    /**
     * 魔法值数字0
     */
    public static final int INT_ZERO = 0;

    /**
     * 魔法值数字1
     */
    public static final int INT_ONE = 1;

    /**
     * 魔法值2
     */
    public static final int INT_TWO = 2;

    /**
     * 魔法值3
     */
    public static final int INT_THREEE = 3;
    /**
     * mcs在机库顶上5分钟内
     */
    public static final long MCS_ALLOW_TIME = 300000L;

    /**
     * 正斜杠
     */
    public static final String FORWARD_SLASH = "/";

    /**
     * 双斜杠
     */
    public static final String DOUBLE_FORWARD_SLASH = "//";

    /**
     * 消息提示
     */
    public static final String MSG = "msg";

    /**
     * 消息结果
     */
    public static final String FLAG = "flag";

    /**
     * 文件返回格式
     */
    public static Map<String, String> responseTypeMap = new HashMap<>(6);

    static {
        //图片格式
        responseTypeMap.put("jpg", "image/jpeg");
        responseTypeMap.put("jpeg", "image/jpeg");
        responseTypeMap.put("jpe", "image/jpeg");
        responseTypeMap.put("png", "image/jpeg");
        //pdf格式
        responseTypeMap.put("pdf", "application/pdf");
        //doc格式
        responseTypeMap.put("doc", "application/msword");
        //kmz
        responseTypeMap.put("kmz", "application/octet-stream");
        //zip
        responseTypeMap.put("zip", " application/x-zip-compressed");
    }

}
