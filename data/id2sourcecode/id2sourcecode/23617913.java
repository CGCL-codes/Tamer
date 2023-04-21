    public static void setPropertyValue(Object bean, String propertyName, Object propertyValue, boolean required, boolean autoConvert) {
        Method writeMethod = null;
        try {
            Class<?> beanClass = bean.getClass();
            PropertyDescriptor propertyDescriptor = getPropertyDescriptor(beanClass, propertyName);
            if (propertyDescriptor == null) if (required) throw new ConfigurationError(beanClass + " does not have a property '" + propertyName + "'"); else return;
            writeMethod = propertyDescriptor.getWriteMethod();
            if (writeMethod == null) throw new UnsupportedOperationException("Cannot write read-only property '" + propertyDescriptor.getName() + "' of " + beanClass);
            Class<?> propertyType = propertyDescriptor.getPropertyType();
            if (propertyValue != null) {
                Class<?> argType = propertyValue.getClass();
                if (!propertyType.isAssignableFrom(argType) && !isWrapperTypeOf(propertyType, propertyValue) && !autoConvert) throw new IllegalArgumentException("ArgumentType mismatch: expected " + propertyType.getName() + ", found " + propertyValue.getClass().getName()); else propertyValue = AnyConverter.convert(propertyValue, propertyType);
            }
            writeMethod.invoke(bean, propertyValue);
        } catch (IllegalAccessException e) {
            throw ExceptionMapper.configurationException(e, writeMethod);
        } catch (InvocationTargetException e) {
            throw ExceptionMapper.configurationException(e, writeMethod);
        }
    }
