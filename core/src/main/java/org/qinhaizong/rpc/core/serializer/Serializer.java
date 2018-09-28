package org.qinhaizong.rpc.core.serializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * @author haizongqin
 */
public interface Serializer {

    /**
     * @param t
     * @param os
     * @param <T>
     */
    <T> void serialize(T t, OutputStream os);

    /**
     * @param in
     * @param t
     * @param <T>
     * @return
     */
    <T> T deserialize(InputStream in, Type t);

}
