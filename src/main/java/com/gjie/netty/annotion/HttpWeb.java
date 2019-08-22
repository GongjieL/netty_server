package com.gjie.netty.annotion;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author j_gong
 * @date 2019/8/22 10:43 AM
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpWeb {

}
