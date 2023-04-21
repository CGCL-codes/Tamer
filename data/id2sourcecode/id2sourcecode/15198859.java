    @Test
    public void testWriterReaderFlow3() throws Throwable {
        outputLogToFile(OhuaLoggerFactory.getLogIDForOperator(ConsumerOperator.class, "TestConsumer", "2") + "-result", Level.INFO);
        OhuaProcessRunner writerRunner = new OhuaProcessRunner(getTestMethodInputDirectory() + "jms-writer-flow.xml");
        SimpleProcessListener writerListener = new SimpleProcessListener();
        writerRunner.register(writerListener);
        new Thread(writerRunner, "jms-writer-process").start();
        writerRunner.submitUserRequest(new UserRequest(UserRequestType.INITIALIZE));
        writerListener.awaitProcessingCompleted();
        writerListener.reset();
        OhuaProcessRunner readerRunner = new OhuaProcessRunner(getTestMethodInputDirectory() + "jms-reader-flow.xml");
        SimpleProcessListener readerListener = new SimpleProcessListener();
        readerRunner.register(readerListener);
        new Thread(readerRunner, "jms-reader-process").start();
        readerRunner.submitUserRequest(new UserRequest(UserRequestType.INITIALIZE));
        readerListener.awaitProcessingCompleted();
        readerListener.reset();
        writerRunner.submitUserRequest(new UserRequest(UserRequestType.START_COMPUTATION));
        Thread.sleep(500);
        readerRunner.submitUserRequest(new UserRequest(UserRequestType.START_COMPUTATION));
        writerRunner.submitUserRequest(new UserRequest(UserRequestType.FINISH_COMPUTATION));
        writerListener.awaitProcessingCompleted();
        waitForShutDown();
        readerRunner.submitUserRequest(new UserRequest(UserRequestType.FINISH_COMPUTATION));
        readerListener.awaitProcessingCompleted();
        writerListener.reset();
        readerListener.reset();
        readerRunner.submitUserRequest(new UserRequest(UserRequestType.SHUT_DOWN));
        readerListener.awaitProcessingCompleted();
        writerRunner.submitUserRequest(new UserRequest(UserRequestType.SHUT_DOWN));
        writerListener.awaitProcessingCompleted();
        assertBaselines();
        Assert.assertFalse(ResourceManager.getInstance().hasExternalActivators());
    }
