    public static Object getObjectAttribute(Object bean, String propertyNames) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object result = bean;
        StringTokenizer propertyTokenizer = new StringTokenizer(propertyNames, PROPERTY_SEPARATOR);
        while (propertyTokenizer.hasMoreElements() && result != null) {
            Class resultClass = result.getClass();
            String currentPropertyName = propertyTokenizer.nextToken();
            PropertyDescriptor propertyDescriptor = getPropertyDescriptor(currentPropertyName, resultClass);
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod == null) {
                throw new IllegalAccessException("User is attempting to " + "read from a property that has no read method.  " + " This is likely a write-only bean property.  Caused " + "by property [" + currentPropertyName + "] on class [" + resultClass + "]");
            }
            result = readMethod.invoke(result, NO_ARGUMENTS_ARRAY);
        }
        return result;
    }
