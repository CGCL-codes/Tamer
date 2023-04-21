    public void testGetWriteMethodFromReadMethod1() throws Exception {
        Method readMethod = MethodUtil.getDeclaredMethod(this.getClass(), "getStr", Constants.EMPTY_CLASS_ARRAY);
        Method writeMethod = JavaBeansUtil.getWriteMethodFromReadMethod(this.getClass(), readMethod);
        assertNotNull(writeMethod);
    }
