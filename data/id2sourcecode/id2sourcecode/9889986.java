    @Test
    public void testGetChannel() throws Exception {
        System.out.println("getChannel");
        AbstractSelectableChannel result = instance.getChannel();
        assertNotNull(result);
    }
