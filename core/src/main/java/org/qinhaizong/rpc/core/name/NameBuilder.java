package org.qinhaizong.rpc.core.name;

import java.lang.reflect.Method;

/**
 * @author haizongqin
 */
public interface NameBuilder {

    /**
     * return name from given method
     *
     * @param method
     * @return
     */
    String getName(Method method);

}
