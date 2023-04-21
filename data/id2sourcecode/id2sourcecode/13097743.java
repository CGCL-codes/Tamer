    public void testInputClosed() throws Exception {
        Logger logger = Logger.getLogger("InputClosedTest");
        logger.setLevel(Level.FINEST);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.FINEST);
        logger.addHandler(consoleHandler);
        SteppingDispatcher steppingDispatcher = new SteppingDispatcher();
        steppingDispatcher.start();
        TestTarget testTarget = new TestTarget(12345);
        testTarget.start();
        SocketChannel channel = testTarget.getSocketChannel();
        BlockingSocketChannel blockingChannel = new BlockingSocketChannel(channel);
        blockingChannel.configureBlocking(false);
        TestChannelHandler testChannelHandler = new TestChannelHandler();
        steppingDispatcher.registerChannel(blockingChannel, testChannelHandler);
        logger.finest("close input from target");
        testTarget.shutdownOutput();
        logger.finest("wait until shutdown propagated through framework");
        testChannelHandler.waitForInputClosed();
        logger.finest("trigger another selection round by blocking write operation");
        ChannelWriter channelWriter = testChannelHandler.getChannelWriter();
        StringToByteBufferTransformer transformer = new StringToByteBufferTransformer();
        transformer.setNextForwarder(channelWriter);
        transformer.forward("this must block and trigger another selection round");
        logger.finest("push dispatcher some selection rounds forward");
        steppingDispatcher.continueDispatcher();
        steppingDispatcher.continueDispatcher();
        assertEquals(1, testChannelHandler.getCloseCounter());
    }
