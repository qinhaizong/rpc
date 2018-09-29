package org.qinhaizong.rpc.core.converter;

import org.qinhaizong.rpc.core.serializer.Serializer;

/**
 * @author haizongqin
 */
public interface MessageConverter {

    /**
     * @return
     */
    String getMimeType();

    /**
     * @return
     */
    Serializer getSerializer();
}
