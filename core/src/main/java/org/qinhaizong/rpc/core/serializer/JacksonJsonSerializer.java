package org.qinhaizong.rpc.core.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ArrayType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

/**
 * @author haizongqin
 */
public class JacksonJsonSerializer implements Serializer {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void serialize(Object arg, OutputStream os) {
        try {
            mapper.writeValue(os, arg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T deserialize(InputStream in, Type t) {
        try {
            if (t instanceof GenericArrayType) {
                Type type = ((GenericArrayType) t).getGenericComponentType();
                ArrayType arrayType = mapper.getTypeFactory().constructArrayType((Class<?>) type);
                return mapper.readValue(in, arrayType);
            }
            if (t instanceof Class) {
                return mapper.readValue(in, (Class<T>) t);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
