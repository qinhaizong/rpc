package org.qinhaizong.rpc.core.converter;

import org.qinhaizong.rpc.core.serializer.JacksonJsonSerializer;
import org.qinhaizong.rpc.core.serializer.Serializer;

/**
 * @author haizongqin
 */
public class Jackson2MessageConverter implements MessageConverter {

    private Serializer serializer = new JacksonJsonSerializer();

    @Override
    public String getMimeType() {
        return "application/json";
    }

    @Override
    public Serializer getSerializer() {
        return serializer;
    }

}
