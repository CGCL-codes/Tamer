    public void testPropertyDescriptorStringMethodMethod_PropertyNameNull() throws SecurityException, NoSuchMethodException, IntrospectionException {
        String propertyName = "PropertyOne";
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);
        Method writeMethod = beanClass.getMethod("set" + propertyName, new Class[] { String.class });
        try {
            new PropertyDescriptor(null, readMethod, writeMethod);
            fail("Should throw IntrospectionException.");
        } catch (IntrospectionException e) {
        }
    }
