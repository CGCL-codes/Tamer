    @Test(timeout = 90000)
    public void testExecuteValidDeployment() throws Exception {
        this.executeInstall(POM, outputDirectory);
        TestLog dummyLog = new TestLog();
        mojo.setLog(dummyLog);
        File tutorial = new File(sgsHome, "tutorial" + File.separator + "tutorial.jar");
        File dest = new File(sgsHome, "deploy" + File.separator + "tutorial.jar");
        FileUtils.copyFile(tutorial, dest);
        new Thread(new Stopper()).start();
        mojo.execute();
        Assert.assertTrue(dummyLog.seen(".*Kernel is ready.*"));
        Assert.assertTrue(dummyLog.seen(".*Controller issued node shutdown.*"));
    }
