    public static <T> PropertyRef<Object> getInstance(T bean, Class<T> beanClass, PropertyDescriptor propertyDescriptor, Object... parameters) {
        String propertyName = propertyDescriptor.getName();
        Method readMethod = propertyDescriptor.getReadMethod();
        Method writeMethod = propertyDescriptor.getWriteMethod();
        PropertyRef<Object> propertyRef = new PropertyRef<Object>(bean, propertyName, Object.class, readMethod, writeMethod, parameters);
        return propertyRef;
    }
