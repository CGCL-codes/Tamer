    @SuppressWarnings("unchecked")
    private <T> void addGetSetProperty(String propertyName, Class<T> type, Method readMethod, Method writeMethod) {
        GetSetProperty<C, T> property = (GetSetProperty<C, T>) ivProperties.get(propertyName);
        if (property == null) property = new GetSetProperty<C, T>(this, propertyName, type);
        if (readMethod != null) property.setReadMethod(ivFClass.getMethod(readMethod), readMethod);
        if (writeMethod != null) property.setWriteMethod(ivFClass.getMethod(writeMethod));
        ivProperties.put(propertyName, property);
    }
