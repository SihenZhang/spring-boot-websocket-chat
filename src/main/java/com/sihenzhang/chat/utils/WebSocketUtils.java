package com.sihenzhang.chat.utils;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;

public final class WebSocketUtils {
    public static String getRemoteAddr(Session session) {
        if (session == null) {
            return null;
        }
        RemoteEndpoint.Async async = session.getAsyncRemote();
        InetSocketAddress address = (InetSocketAddress) getFieldInstance(async, "base#socketWrapper#socket#sc#remoteAddress");
        if (address == null) {
            return null;
        }
        return address.getAddress().getHostAddress();
    }

    private static Object getFieldInstance(Object obj, String fieldPath) {
        String[] fields = fieldPath.split("#");
        for (String field : fields) {
            obj = getField(obj, obj.getClass(), field);
            if (obj == null) {
                return null;
            }
        }
        return obj;
    }

    private static Object getField(Object obj, Class<?> clazz, String fieldName) {
        while (clazz != Object.class) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (Exception ignored) {
                // do nothing
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }
}
