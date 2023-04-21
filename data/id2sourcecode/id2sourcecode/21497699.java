    public void setValue(Object target, Object v) throws InvocationTargetException, IllegalAccessException {
        if (isReadOnly()) {
            throw new IllegalAccessError("read-only property can not be write");
        }
        setter.invoke(target, new Object[] { v });
    }
