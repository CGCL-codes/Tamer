    @Test
    public void testAppKernelAppContext() {
        final AppContextKernelProvider provider = new AppContextKernelProvider();
        this.generateKernel(this.getSimpleConfig(), new IPdsKernelProvider[] { provider });
        this.assertRunning();
        Assert.assertNotNull("Data Manager must not be null", this.testKernel.getAppKernelAppContext().getDataManager());
        Assert.assertNotNull("Task Manager must not be null", this.testKernel.getAppKernelAppContext().getTaskManager());
        Assert.assertNotNull("Channel Manager must not be null", this.testKernel.getAppKernelAppContext().getChannelManager());
        Assert.assertNull("Random manager must be null", this.testKernel.getAppKernelAppContext().getManager(SimpleManager.class));
        Assert.assertNull("Random service must be null", this.testKernel.getAppKernelAppContext().getService(SimpleService.class));
        this.assertCleanShutdown();
        Assert.assertTrue("get channel manager must be called", provider.getChannelManagerCalled);
        Assert.assertTrue("get data manager must be called", provider.getDataManagerCalled);
        Assert.assertTrue("get task manager must be called", provider.getTaskManagerCalled);
        Assert.assertTrue("get manager must be called", provider.getManagerCalled);
        Assert.assertTrue("get service must be called", provider.getServiceCalled);
        Assert.assertTrue("shutdown services manager must be called", provider.shutdownCalled);
    }
