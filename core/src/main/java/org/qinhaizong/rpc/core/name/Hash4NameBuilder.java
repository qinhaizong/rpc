package org.qinhaizong.rpc.core.name;

import java.lang.reflect.Method;
import java.nio.charset.Charset;

/**
 * ref. https://lemire.me/blog/2015/10/22/faster-hashing-without-effort/
 * <p>
 * ref. https://github.com/lemire/microbenchmarks/blob/master/src/main/java/me/lemire/hashing/InterleavedHash.java
 *
 * @author haizongqin
 */
public class Hash4NameBuilder extends ClassMethodNameBuilder {

    @Override
    public String getName(Method method) {
        byte[] bytes = super.getName(method).getBytes(Charset.forName("UTF-8"));
        int len = bytes.length;
        int h = 1;
        int i = 0;
        for (; i + 3 < len; i += 4) {
            h = 31 * 31 * 31 * 31 * h + 31 * 31 * 31 * bytes[i] + 31 * 31 * bytes[i + 1] + 31 * bytes[i + 2] + bytes[i + 3];
        }
        for (; i < len; i++) {
            h = 31 * h + bytes[i];
        }
        return Integer.toHexString(h);
    }

}
