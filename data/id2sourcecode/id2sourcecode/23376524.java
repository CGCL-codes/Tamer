    public void testPropertyDescriptorStringMethodMethod_ReadMethodInvalid() throws SecurityException, NoSuchMethodException, IntrospectionException {
        String propertyName = "PropertyOne";
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        String anotherProp = "PropertyTwo";
        Method readMethod = beanClass.getMethod("get" + anotherProp, (Class[]) null);
        Method writeMethod = beanClass.getMethod("set" + propertyName, new Class[] { String.class });
        try {
            new PropertyDescriptor(propertyName, readMethod, writeMethod);
            fail("Should throw IntrospectionException.");
        } catch (IntrospectionException e) {
        }
    }
