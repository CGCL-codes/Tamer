    public void testGetWriteMethodFromReadMethod2() throws Exception {
        Method readMethod = MethodUtil.getDeclaredMethod(this.getClass(), "isFlag", Constants.EMPTY_CLASS_ARRAY);
        Method writeMethod = JavaBeansUtil.getWriteMethodFromReadMethod(this.getClass(), readMethod);
        assertNotNull(writeMethod);
    }
