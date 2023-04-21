    @Test(expected = ManagerNotFoundException.class)
    public void testGetChannelManagerBeforeInit() {
        AppContext.getChannelManager();
    }
