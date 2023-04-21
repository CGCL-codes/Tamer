    @Test(expected = ManagerNotFoundException.class)
    public void testGetChannelManagerWithEmptyManagerLocator() {
        initEmptyAppContext();
        InternalContext.setManagerLocator(managerLocator);
        AppContext.getChannelManager();
    }
