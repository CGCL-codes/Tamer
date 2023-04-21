    public final void testBean() throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
        Bean bean = new Bean();
        PropertyDescriptor[] beanProperties = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
        for (PropertyDescriptor pd : beanProperties) {
            Method writeMethod = pd.getWriteMethod();
            Method readMethod = pd.getReadMethod();
            if (writeMethod != null && readMethod != null) {
                assertTrue(pd.getPropertyType().equals(Integer.class));
                assertEquals(readMethod.getName(), "getValue");
                Class[] setterArgs = { pd.getPropertyType() };
                readMethod.invoke(bean, GETTER_ARGS);
                writeMethod.invoke(bean, new Integer[] { 1 });
            }
        }
    }
