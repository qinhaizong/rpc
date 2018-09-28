package org.qinhaizong.rpc.core.client;

import com.fasterxml.jackson.databind.ObjectMapper;

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

    private final String serviceUrl;

    public RpcInvocationHandler(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String url = new StringBuilder().append(serviceUrl).append("/").append(method.getDeclaringClass().getCanonicalName()).append("/").append(method.getName()).toString();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        if (Objects.nonNull(args) && args.length > 0) {
            try (OutputStream os = connection.getOutputStream()) {
                if (args.length == 1) {
                    objectMapper.writeValue(os, args[0]);
                }
                if (args.length > 1) {
                    objectMapper.writeValue(os, args);
                }
                os.flush();
            }
        }
        Object value = null;
        Class<?> returnType = method.getReturnType();
        if (!Void.class.getSimpleName().equalsIgnoreCase(returnType.getName())) {
            try (InputStream in = connection.getInputStream()) {
                value = objectMapper.readValue(in, returnType);
            }
        }
        return value;
    }

}
