    public void testPropertyDescriptorStringMethodMethod_WriteMethodNull() throws SecurityException, NoSuchMethodException, IntrospectionException {
        String propertyName = "PropertyOne";
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);
        Method writeMethod = null;
        PropertyDescriptor pd = new PropertyDescriptor(propertyName, readMethod, writeMethod);
        assertEquals(String.class, pd.getPropertyType());
        assertEquals("get" + propertyName, pd.getReadMethod().getName());
        assertNull(pd.getWriteMethod());
        assertFalse(pd.isBound());
        assertFalse(pd.isConstrained());
        assertNull(pd.getPropertyEditorClass());
        assertEquals(propertyName, pd.getDisplayName());
        assertEquals(propertyName, pd.getName());
        assertEquals(propertyName, pd.getShortDescription());
        assertNotNull(pd.attributeNames());
        assertFalse(pd.isExpert());
        assertFalse(pd.isHidden());
        assertFalse(pd.isPreferred());
    }
