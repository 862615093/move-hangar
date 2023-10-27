package snegrid.move.hangar.netty.model;

import snegrid.move.hangar.netty.message.Message;

/**
 * 抽象实体
 *
 * @author wangwei
 */
public abstract class AbstractEntity {

    private String id;

    private AbstractEntity parent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 处理返回请求
     */
    public abstract void handleRequest(Message message);

    /**
     * 移除子对象
     */
    public abstract void removeChild(AbstractEntity entity);

    public void destroy() {
        if (parent != null) {
            parent.removeChild(this);
        }
        close();
    }

    protected void close() {
    }

    public AbstractEntity getParent() {
        return parent;
    }

    public void setParent(AbstractEntity parent) {
        this.parent = parent;
    }
}
