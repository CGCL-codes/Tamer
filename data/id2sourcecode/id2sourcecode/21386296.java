    @Test
    @BrokerNature(protocol = BrokerProtocol.TCP, port = "61616")
    public void testCrashRestartJMSReader() throws Throwable {
        JMSCrashRestartRunner readerRunner = new JMSCrashRestartRunner();
        readerRunner.setPathToFlow(getTestMethodInputDirectory() + "jms-reader-flow.xml");
        readerRunner.applyDefaultConfiguration(getTestMethodInputDirectory());
        OhuaProcessRunner runner = new OhuaProcessRunner(getTestMethodInputDirectory() + "jms-writer-flow.xml");
        SimpleProcessListener listener = new SimpleProcessListener();
        runner.register(listener);
        new Thread(runner, "jms-writer-process").start();
        runner.submitUserRequest(new UserRequest(UserRequestType.INITIALIZE));
        listener.awaitProcessingCompleted();
        listener.reset();
        runner.submitUserRequest(new UserRequest(UserRequestType.START_COMPUTATION));
        readerRunner.crashAndRestart(10000, false, ProcessNature.USER_DRIVEN);
        System.out.println("reader process is done");
        listener.awaitProcessingCompleted();
        listener.reset();
        runner.submitUserRequest(new UserRequest(UserRequestType.SHUT_DOWN));
        listener.awaitProcessingCompleted();
        assertBaselines();
    }
