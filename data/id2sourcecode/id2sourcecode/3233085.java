    @Test
    public void testGetChannel() throws Exception {
        System.out.println("getChannel");
        DatagramChannel result = (DatagramChannel) instance.getChannel();
        assertNotNull(result);
    }
