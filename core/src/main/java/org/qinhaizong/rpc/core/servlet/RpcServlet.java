package org.qinhaizong.rpc.core.servlet;

import org.qinhaizong.rpc.core.converter.MessageConverter;
import org.qinhaizong.rpc.core.name.NameBuilder;
import org.qinhaizong.rpc.core.serializer.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import sun.reflect.generics.reflectiveObjects.GenericArrayTypeImpl;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext;

public class RpcServlet implements Servlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServlet.class);

    private Map<String, ObjectMethod> map;

    private NameBuilder builder = SpringFactoriesLoader.loadFactories(NameBuilder.class, ClassUtils.getDefaultClassLoader()).stream().findFirst().get();

    private MessageConverter converter = SpringFactoriesLoader.loadFactories(MessageConverter.class, ClassUtils.getDefaultClassLoader()).stream().findFirst().get();

    @Override
    public void init(ServletConfig config) throws ServletException {
        WebApplicationContext context = getWebApplicationContext(config.getServletContext());
        String interfaces = config.getInitParameter("interfaces");
        if (interfaces != null && interfaces.length() > 0) {
            //@formatter:off
            map = Arrays.stream(interfaces.split(",")).map(String::trim).map(s -> {
                try {
                    return Class.forName(s, false, Thread.currentThread().getContextClassLoader());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }).filter(Objects::nonNull)
            .flatMap(i -> Arrays.stream(i.getDeclaredMethods()))
            .collect(Collectors.toMap(builder::getName, m -> {
                Class<?> clazz = m.getDeclaringClass();
                Method method = BeanUtils.findDeclaredMethod(clazz, m.getName(), m.getParameterTypes());
                return new ObjectMethod(context.getBean(clazz), method);
            }));
            //@formatter:on
            LOGGER.info("beans: {}", map);
        }
        if (Objects.isNull(map)) {
            map = Collections.EMPTY_MAP;
        }
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String classMethod = Arrays.stream(request.getRequestURI().split("/")).filter(StringUtils::hasText).collect(Collectors.joining("/"));
        ObjectMethod objectMethod = map.get(classMethod);
        if ("POST".equalsIgnoreCase(request.getMethod()) && converter.getMimeType().equals(request.getContentType()) && Objects.nonNull(objectMethod)) {
            Serializer serializer = converter.getSerializer();
            Method method = objectMethod.getMethod();
            Class<?>[] types = method.getParameterTypes();
            Object target = objectMethod.getObject();
            Object returnValue;
            try (InputStream in = req.getInputStream()) {
                switch (types.length) {
                    case 0:
                        returnValue = ReflectionUtils.invokeMethod(method, target);
                        break;
                    case 1:
                        Object arg = serializer.deserialize(in, types[0]);
                        returnValue = ReflectionUtils.invokeMethod(method, target, arg);
                        break;
                    default:
                        Object[] args = serializer.deserialize(in, GenericArrayTypeImpl.make(Object.class));
                        returnValue = ReflectionUtils.invokeMethod(method, target, args);
                        break;
                }
            }
            if (Objects.nonNull(returnValue)) {
                try (OutputStream os = response.getOutputStream()) {
                    serializer.serialize(returnValue, os);
                }
            }
        } else {
            response.setStatus(204);
        }
    }

    @Override
    public String getServletInfo() {
        return "rpc.servlet:qinhaizong:v1";
    }

    @Override
    public void destroy() {
    }

    public class ObjectMethod {

        private final Object object;

        private final Method method;

        public ObjectMethod(Object object, Method method) {
            this.object = object;
            this.method = method;
        }

        public Object getObject() {
            return object;
        }

        public Method getMethod() {
            return method;
        }

    }

}
