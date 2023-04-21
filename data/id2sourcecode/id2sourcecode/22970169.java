    public static PropertyDescriptor createPropertyDescriptor(Class<?> beanClass, String propertyName, String readMethodName, String writeMethodName, boolean bound, boolean constrained, boolean isTransient) {
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(propertyName, beanClass, readMethodName, writeMethodName);
            descriptor.setBound(bound);
            descriptor.setConstrained(constrained);
            if (isTransient) descriptor.setValue("transient", Boolean.TRUE);
            return descriptor;
        } catch (IntrospectionException ex) {
            throw new RuntimeException("Error creating PropertyDescriptor for " + beanClass.getCanonicalName() + "." + propertyName, ex);
        }
    }
