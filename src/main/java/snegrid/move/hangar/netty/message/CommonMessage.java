package snegrid.move.hangar.netty.message;

import lombok.ToString;
import snegrid.move.hangar.netty.model.AbstractEntity;

import java.util.LinkedHashMap;

/**
 * 通用协议
 *
 * @author wangwei
 */
public class CommonMessage extends Message {

    private LinkedHashMap<String, Object> params;

    public CommonMessage(int code) {
        super(code);
        this.params = new LinkedHashMap<>();
    }

    public CommonMessage(int code, AbstractEntity entity) {
        super(code, entity);
        this.params = new LinkedHashMap<>();
    }

    public CommonMessage put(String name, String value) {
        params.put(name, value);
        return this;
    }

    public Object get(String name) {
        return params.get(name);
    }

    public LinkedHashMap<String, Object> getParams() {
        return params;
    }

    public void setParams(LinkedHashMap<String, Object> params) {
        this.params = params;
    }
}
