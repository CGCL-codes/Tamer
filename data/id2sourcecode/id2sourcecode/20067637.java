    private void initEmptyAppContext() {
        managerLocator = EasyMock.createMock(ManagerLocator.class);
        ManagerNotFoundException m = new ManagerNotFoundException("not found");
        EasyMock.expect(managerLocator.getDataManager()).andThrow(m);
        EasyMock.expect(managerLocator.getChannelManager()).andThrow(m);
        EasyMock.expect(managerLocator.getTaskManager()).andThrow(m);
        EasyMock.expect(managerLocator.getManager(Object.class)).andThrow(m);
        EasyMock.replay(managerLocator);
    }
