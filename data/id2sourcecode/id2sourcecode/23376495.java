    public void testEquals() throws IntrospectionException, SecurityException, NoSuchMethodException {
        String propertyName = "PropertyOne";
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        PropertyDescriptor pd = new PropertyDescriptor(propertyName, beanClass);
        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);
        Method writeMethod = beanClass.getMethod("set" + propertyName, new Class[] { String.class });
        PropertyDescriptor pd2 = new PropertyDescriptor(propertyName, readMethod, writeMethod);
        pd.setName("different name");
        assertTrue(pd.equals(pd2));
        assertTrue(pd.equals(pd));
        assertTrue(pd2.equals(pd));
        assertFalse(pd.equals(null));
    }
