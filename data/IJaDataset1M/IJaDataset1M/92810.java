package org.granite.messaging.service.tide;

import java.util.Set;
import org.granite.config.GraniteConfigException;
import org.granite.util.ClassUtil;

/**
 * @author Franck WOLFF
 */
public class TideComponentInstanceOfMatcher implements TideComponentMatcher {

    private final boolean disabled;

    private final Class<?> parentClass;

    public TideComponentInstanceOfMatcher(String type, boolean disabled) {
        try {
            parentClass = ClassUtil.forName(type);
            this.disabled = disabled;
        } catch (Exception e) {
            throw new GraniteConfigException("Could not instantiate instanceof matcher parent class: " + type, e);
        }
    }

    public boolean matches(String name, Set<Class<?>> classes, Object instance, boolean disabled) {
        for (Class<?> clazz : classes) {
            if (disabled == this.disabled && parentClass.isAssignableFrom(clazz)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Instanceof matcher: " + parentClass.getName() + (disabled ? " (disabled)" : "");
    }
}
