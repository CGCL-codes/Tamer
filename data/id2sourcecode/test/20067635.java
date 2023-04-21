    private void initStableAppContext() {
        managerLocator = EasyMock.createMock(ManagerLocator.class);
        dataManager = EasyMock.createNiceMock(DataManager.class);
        channelManager = EasyMock.createNiceMock(ChannelManager.class);
        taskManager = EasyMock.createNiceMock(TaskManager.class);
        arbitraryManager = new Object();
        EasyMock.expect(managerLocator.getDataManager()).andReturn(dataManager);
        EasyMock.expect(managerLocator.getChannelManager()).andReturn(channelManager);
        EasyMock.expect(managerLocator.getTaskManager()).andReturn(taskManager);
        EasyMock.expect(managerLocator.getManager(Object.class)).andReturn(arbitraryManager);
        EasyMock.replay(managerLocator);
    }
