package org.qinhaizong.rpc.core.name;

import org.springframework.util.DigestUtils;

import java.lang.reflect.Method;
import java.nio.charset.Charset;

/**
 * @author haizongqin
 */
public class Md5HashNameBuilder extends ClassMethodNameBuilder {

    @Override
    public String getName(Method method) {
        return DigestUtils.md5DigestAsHex(super.getName(method).getBytes(Charset.forName("UTF-8")));
    }

}
