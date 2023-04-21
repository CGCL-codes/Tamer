    public static void setObjectAttribute(Object bean, String propertyNames, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        Object result = bean;
        String propertyName = propertyNames;
        int indexOfLastPropertySeparator = propertyName.lastIndexOf(PROPERTY_SEPARATOR);
        if (indexOfLastPropertySeparator >= 0) {
            String embeddedProperties = propertyName.substring(0, indexOfLastPropertySeparator);
            propertyName = propertyName.substring(indexOfLastPropertySeparator + PROPERTY_SEPARATOR.length());
            result = getObjectAttribute(result, embeddedProperties);
        }
        Class resultClass = result.getClass();
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(propertyName, resultClass);
        Method writeMethod = propertyDescriptor.getWriteMethod();
        if (writeMethod == null) {
            throw new IllegalAccessException("User is attempting to write " + "to a property that has no write method.  This is likely " + "a read-only bean property.  Caused by property [" + propertyName + "] on class [" + resultClass + "]");
        }
        writeMethod.invoke(result, new Object[] { value });
    }
