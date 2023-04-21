    public void testGetReadMethodFromWriteMethod2() throws Exception {
        Method writeMethod = MethodUtil.getDeclaredMethod(this.getClass(), "setFlag", new Class[] { Boolean.TYPE });
        Method readMethod = JavaBeansUtil.getReadMethodFromWriteMethod(this.getClass(), writeMethod);
        assertNotNull(readMethod);
    }
