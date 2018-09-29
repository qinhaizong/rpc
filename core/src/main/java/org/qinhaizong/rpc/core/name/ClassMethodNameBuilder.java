package org.qinhaizong.rpc.core.name;

import java.lang.reflect.Method;

/**
 * @author haizongqin
 */
public class ClassMethodNameBuilder implements NameBuilder {

    @Override
    public String getName(Method method) {
        return new StringBuilder().append(method.getDeclaringClass().getCanonicalName()).append("/").append(method.getName()).toString();
    }

}
