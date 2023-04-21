    public void testPropertyDescriptorStringMethodMethod_ReadMethodNull() throws SecurityException, NoSuchMethodException, IntrospectionException {
        String propertyName = "PropertyOne";
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        Method readMethod = null;
        Method writeMethod = beanClass.getMethod("set" + propertyName, new Class[] { String.class });
        PropertyDescriptor pd = new PropertyDescriptor(propertyName, readMethod, writeMethod);
        assertEquals(String.class, pd.getPropertyType());
        assertNull(pd.getReadMethod());
        assertEquals("set" + propertyName, pd.getWriteMethod().getName());
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
