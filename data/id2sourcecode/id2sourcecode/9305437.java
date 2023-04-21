    public static <T> Method getWriteMethodFromReadMethod(Class<T> c, Method readMethod) {
        Assertion.notNull(c);
        Assertion.notNull(readMethod);
        final String methodName = readMethod.getName();
        String propertyName = getPropertyName(methodName);
        String writeMethodName = Constants.SET + capitalize(propertyName);
        return MethodUtil.getDeclaredMethodNoException(c, writeMethodName, new Class<?>[] { readMethod.getReturnType() });
    }
