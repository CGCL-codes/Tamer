    public void testSetPropertyEditorClass() throws SecurityException, NoSuchMethodException, IntrospectionException {
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        String propertyName = "PropertyOne";
        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);
        Method writeMethod = beanClass.getMethod("set" + propertyName, new Class[] { String.class });
        PropertyDescriptor pd = new PropertyDescriptor(propertyName, readMethod, writeMethod);
        Class<? extends String> editorClass = "This is an invalid editor class".getClass();
        pd.setPropertyEditorClass(editorClass);
        assertSame(editorClass, pd.getPropertyEditorClass());
    }
