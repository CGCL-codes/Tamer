    public Object getValue(Object obj) throws InvocationTargetException, IllegalAccessException {
        if (isWriteOnly()) {
            throw new IllegalAccessError("write-only property can not be read");
        }
        return getter.invoke(obj);
    }
