package org.qinhaizong.rpc.core.client;

import org.qinhaizong.rpc.core.name.NameBuilder;
import org.qinhaizong.rpc.core.serializer.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ClassUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

/**
 * @author haizongqin
 */
public class RpcInvocationHandler implements InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcInvocationHandler.class);

    private final String serviceUrl;

    private NameBuilder builder = SpringFactoriesLoader.loadFactories(NameBuilder.class, ClassUtils.getDefaultClassLoader()).stream().findFirst().get();

    private Serializer serializer = SpringFactoriesLoader.loadFactories(Serializer.class, ClassUtils.getDefaultClassLoader()).stream().findFirst().get();

    public RpcInvocationHandler(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String url = new StringBuilder().append(serviceUrl).append("/").append(builder.getName(method)).toString();
        LOGGER.info("> curl -XPOST '{}'", url);
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        if (Objects.nonNull(args) && args.length > 0) {
            try (OutputStream os = connection.getOutputStream()) {
                if (args.length == 1) {
                    serializer.serialize(args[0], os);
                }
                if (args.length > 1) {
                    serializer.serialize(args, os);
                }
                os.flush();
            }
        }
        int responseCode = connection.getResponseCode();
        LOGGER.info("< {}", responseCode);
        Object value = null;
        Class<?> returnType = method.getReturnType();
        if (!Void.class.getSimpleName().equalsIgnoreCase(returnType.getName())) {
            try (InputStream in = connection.getInputStream()) {
                value = serializer.deserialize(in, returnType);
            }
        }
        connection.disconnect();
        return value;
    }

}
