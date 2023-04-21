    public static ExtendedPropertyDescriptor newPropertyDescriptor(String propertyName, Class beanClass) throws IntrospectionException {
        Method readMethod = BeanUtils.getReadMethod(beanClass, propertyName);
        Method writeMethod = null;
        if (readMethod == null) {
            throw new IntrospectionException("No getter for property " + propertyName + " in class " + beanClass.getName());
        }
        writeMethod = BeanUtils.getWriteMethod(beanClass, propertyName, readMethod.getReturnType());
        return new ExtendedPropertyDescriptor(propertyName, readMethod, writeMethod);
    }
