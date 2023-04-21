    public void testPropertyDescriptorStringMethodMethod_WriteMethodInvalid() throws SecurityException, NoSuchMethodException, IntrospectionException {
        String propertyName = "PropertyOne";
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        String anotherProp = "PropertyTwo";
        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);
        Method writeMethod = beanClass.getMethod("set" + anotherProp, new Class[] { Integer.class });
        try {
            new PropertyDescriptor(propertyName, readMethod, writeMethod);
            fail("Should throw IntrospectionException.");
        } catch (IntrospectionException e) {
        }
    }
