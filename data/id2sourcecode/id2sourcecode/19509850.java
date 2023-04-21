    @Test
    public void testGetChannel() throws Exception {
        System.out.println("getChannel");
        AbstractIPSampler instance = new AbstractIPSamplerImpl();
        AbstractSelectableChannel expResult = null;
        AbstractSelectableChannel result = instance.getChannel();
        assertEquals(expResult, result);
    }
