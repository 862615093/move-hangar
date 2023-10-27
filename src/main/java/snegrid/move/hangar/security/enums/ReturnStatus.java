package snegrid.move.hangar.security.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回状态
 *
 * @author zhaojl
 */
@Getter
@AllArgsConstructor
public enum ReturnStatus {

    /**
     * code 操作成功
     **/
    SUCCESS(0, "操作成功"),
    /**
     * 用户名不存在
     **/
    USER_NON_EXISTENT(1001, "用户名不存在"),
    /**
     * 用户不具有管理员权限
     **/
    USER_NO_ADMIN_PREMISSION(1002, "用户不具有管理员权限"),
    /**
     * 密码不正确
     **/
    USER_INCORRECT_PASSWORD(1003, "密码不正确"),
    /**
     * 用户未登录
     **/
    USER_NOT_LOGGED_IN(1004, "用户未登录"),
    /**
     * 账号已经在其他地方登录
     **/
    USER_HAS_LOGGED_IN(1005, "token过期或者账号已经在其他地方登录"),

    /**
     * 该机场未设置默认航线
     **/
    NOT_SET_DEFAULT_ROUTE(1006, "航线id不能为空"),

    /**
     * 该机场未设置默认航线
     **/
    NOT_EXIST_ROUTE_TASK(1007, "不存在正在执行的航线任务"),
    /**
     * 系统中不存在该设备
     */
    DEVICE_NO_EXIST(1050, "系统中不存在该设备或者设备未匹配"),
    /**
     * 无效指令
     */
    NO_AVAIL_CMD(1051, "无效指令"),
    /**
     * 同步指令执行失败
     */
    SYNC_FAILED_CMD(1052, "同步指令执行失败"),
    /**
     * 新的web socket会话加入
     */
    WS_CONNECT_SUCESS(1060, "新的web socket会话加入");


    /**
     * code 编码
     **/
    private Integer code;
    /**
     * 消息内容
     **/
    private String msg;
}
