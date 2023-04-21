    public void testSetConstrained_false() throws SecurityException, NoSuchMethodException, IntrospectionException {
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        String propertyName = "PropertyOne";
        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);
        Method writeMethod = beanClass.getMethod("set" + propertyName, new Class[] { String.class });
        PropertyDescriptor pd = new PropertyDescriptor(propertyName, readMethod, writeMethod);
        pd.setConstrained(false);
        assertFalse(pd.isConstrained());
        assertFalse(pd.isBound());
        assertNull(pd.getPropertyEditorClass());
        assertEquals(propertyName, pd.getDisplayName());
        assertEquals(propertyName, pd.getName());
        assertEquals(propertyName, pd.getShortDescription());
        assertNotNull(pd.attributeNames());
        assertFalse(pd.isExpert());
        assertFalse(pd.isHidden());
        assertFalse(pd.isPreferred());
    }
