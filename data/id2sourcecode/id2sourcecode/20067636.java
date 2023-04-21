    private void initUnstableAppContext() {
        initStableAppContext();
        EasyMock.reset(managerLocator);
        EasyMock.expect(managerLocator.getDataManager()).andReturn(dataManager).andReturn(null);
        EasyMock.expect(managerLocator.getChannelManager()).andReturn(channelManager).andReturn(null);
        EasyMock.expect(managerLocator.getTaskManager()).andReturn(taskManager).andReturn(null);
        EasyMock.expect(managerLocator.getManager(Object.class)).andReturn(arbitraryManager).andReturn(null);
        EasyMock.replay(managerLocator);
    }
