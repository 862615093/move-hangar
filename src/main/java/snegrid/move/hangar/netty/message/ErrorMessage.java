package snegrid.move.hangar.netty.message;

import snegrid.move.hangar.netty.model.AbstractEntity;

/**
 * 自定义错误信息
 *
 * @author wangwei
 */
public class ErrorMessage extends Message {

    private String msg;

    public ErrorMessage(AbstractEntity entity, String msg) {
        super(MessageType.Has_Error, entity);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
