    public void testGetReadMethodFromWriteMethod1() throws Exception {
        Method writeMethod = MethodUtil.getDeclaredMethod(this.getClass(), "setStr", new Class[] { String.class });
        Method readMethod = JavaBeansUtil.getReadMethodFromWriteMethod(this.getClass(), writeMethod);
        assertNotNull(readMethod);
    }
