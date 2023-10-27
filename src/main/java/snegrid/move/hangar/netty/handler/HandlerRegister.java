package snegrid.move.hangar.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import snegrid.move.hangar.exception.ServiceException;
import snegrid.move.hangar.netty.message.Message;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 业务注册器
 *
 * @author wangwei
 */
public final class HandlerRegister {

    private static final Logger logger = LoggerFactory.getLogger(HandlerRegister.class);

    private static Map<Integer, Adaptor> invokeMap = new ConcurrentHashMap<>();

    public static void register(NettyHandler nettyHandler) {
        for (Method method : nettyHandler.getClass().getDeclaredMethods()) {
            Handle anno = method.getAnnotation(Handle.class);
            if (anno != null) {
                if (!invokeMap.containsKey(anno.value())) {
                    invokeMap.put(anno.value(), new Adaptor(anno.value(), nettyHandler, method));
                } else {
                    throw new ServiceException("协议重复 :" + anno.value());
                }
            }
        }
    }

    public static Message invoke(int code, Object[] params) throws Exception {
        if (!invokeMap.containsKey(code)) {
            logger.error("找不到该协议的处理方法code=【{}】", code);
            return null;
        }
        Adaptor adaptor = invokeMap.get(code);
        return adaptor.invoke(params);
    }

    static class Adaptor {
        private int code;
        private Object target;
        private Method method;

        public Adaptor(int code, Object target, Method method) {
            this.setCode(code);
            this.target = target;
            this.method = method;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public Message invoke(Object[] params) throws Exception {
            if (params.length != method.getParameterCount()) {
                throw new ServiceException("参数不匹配：" + method.getName());
            }
            for (int i = 0; i < params.length; i++) {
                params[i] = getParamValue(method.getParameterTypes()[i], params[i]);
            }
            return (Message) this.method.invoke(target, params);
        }

        private Object getParamValue(Class<?> clazz, Object object) {
            if ((clazz == Integer.TYPE) || (clazz == Integer.class)) {
                return Integer.valueOf(object.toString());
            } else if ((clazz == Long.TYPE) || (clazz == Long.class)) {
                return Long.valueOf(object.toString());
            } else if ((clazz == Byte.TYPE) || (clazz == Byte.class)) {
                return Byte.valueOf(object.toString());
            } else if ((clazz == Double.TYPE) || (clazz == Double.class)) {
                return Double.valueOf(object.toString());
            } else if ((clazz == Short.TYPE) || (clazz == Short.class)) {
                return Short.valueOf(object.toString());
            } else if ((clazz == Float.TYPE) || (clazz == Float.class)) {
                return Float.valueOf(object.toString());
            } else if (clazz == String.class) {
                return object.toString();
            }
            return null;
        }
    }
}
