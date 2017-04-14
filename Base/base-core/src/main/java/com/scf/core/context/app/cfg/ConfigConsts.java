/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.core.context.app.cfg;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自动对子类中的静态类变量进行初始化。如将a.b的值赋予a_b。
 *
 */
public class ConfigConsts {

    private static final Logger _logger = LoggerFactory.getLogger(ConfigConsts.class);

    public static void init(ConfigParams params, Class constsClass) throws Exception {
        Field[] consts = constsClass.getFields();
        for (Field f : consts) {
            if (Modifier.isStatic(f.getModifiers())) {
                String fn = f.getName();
                String _fn = fn.replace('_', '.').toLowerCase();
                Object v = f.get(null);
                Object _v = params.get(_fn, v);
                _logger.info("Set const var " + constsClass.getSimpleName() + "." + fn + " = " + _v + "[default: " + v + "]");
                f.set(null, _v);
            }
        }
    }
}
