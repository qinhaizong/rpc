package org.qinhaizong.rpc.core.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author haizongqin
 */
public class RpcClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);

    public static ConcurrentHashMap<Class, Object> pool = new ConcurrentHashMap<>();

    public static <T> T from(Class<T> t, String serviceUrl) {
        if (!pool.contains(t)) {
            LOGGER.info("create {} proxy", t.getCanonicalName());
            pool.put(t, Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{t}, new RpcInvocationHandler(serviceUrl)));
        }
        return (T) pool.get(t);
    }

}
