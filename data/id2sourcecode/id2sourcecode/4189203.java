    public static Class<?> resolvePropertyType(final Class<?> targetClass, final Method readMethod, final Method writeMethod) {
        Class<?> type = null;
        if (readMethod != null) {
            type = resolveReturnType(targetClass, readMethod);
        }
        if ((type == null) && (writeMethod != null)) {
            type = resolveParameterType(targetClass, writeMethod);
        }
        return type;
    }
