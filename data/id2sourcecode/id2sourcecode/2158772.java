    protected void addPropertyDescriptor(Class<?> pBeanClazz, String pPropertyName, String pReadMethodName, String pWriteMethodName) {
        Method readMethod = getOptionalAccessibleMethod(pBeanClazz, pReadMethodName);
        Method writeMethod = getOptionalAccessibleMethod(pBeanClazz, pWriteMethodName, readMethod.getReturnType());
        try {
            fPropertyDescriptors.add(new PropertyDescriptor(pPropertyName, readMethod, writeMethod));
        } catch (IntrospectionException ex) {
            throw new RuntimeException("fatal", ex);
        }
    }
