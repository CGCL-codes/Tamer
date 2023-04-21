    public PropertyDescriptor getBeanProperty(Class bean, String propertyName) {
        PropertyDescriptor prop = getPropertyDescriptor(bean, propertyName);
        if (prop != null) {
            Method write = prop.getWriteMethod();
            Method read = prop.getReadMethod();
            if (write != null && read != null) {
                Class[] parameterTypes = write.getParameterTypes();
                Class returnType = read.getReturnType();
                if (parameterTypes.length == 1 && parameterTypes[0].equals(returnType)) {
                    return prop;
                }
            }
        }
        return null;
    }
