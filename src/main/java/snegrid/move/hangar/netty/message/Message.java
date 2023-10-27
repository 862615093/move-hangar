package snegrid.move.hangar.netty.message;

import snegrid.move.hangar.netty.model.AbstractEntity;

/**
 * 消息接口
 *
 * @author wangwei
 */
public abstract class Message {

    /**
     * 消息号
     */
    private int code;

    /**
     * 消息体
     */
    private transient AbstractEntity entity;

    public Message(int code) {
        this.code = code;
    }

    public Message(int code, AbstractEntity entity) {
        this(code);
        this.entity = entity;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public AbstractEntity getEntity() {
        return entity;
    }

    public void setEntity(AbstractEntity entity) {
        this.entity = entity;
    }
}
