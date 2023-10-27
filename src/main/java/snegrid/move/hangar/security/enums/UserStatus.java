package snegrid.move.hangar.security.enums;

/**
 * 用户状态
 *
 * @author drone
 */
public enum UserStatus {

    /**
     * 用户状态
     */
    OK("0", "正常"), DISABLE("1", "停用"), DELETED("2", "删除");
    /**
     * code
     */
    private final String code;
    /**
     * 信息
     */
    private final String info;

    UserStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
