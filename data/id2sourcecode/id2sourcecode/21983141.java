    public void testChannelLock() throws Exception {
        Logger logger = Logger.getLogger("CachedOpsTest");
        logger.setLevel(Level.FINEST);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.FINEST);
        logger.addHandler(consoleHandler);
        CacheStopDispatcher cacheStopDispatcher = new CacheStopDispatcher();
        cacheStopDispatcher.start();
        TestTarget testTarget = new TestTarget(12345);
        testTarget.start();
        SocketChannel channel = testTarget.getSocketChannel();
        BlockingSocketChannel blockingChannel = new BlockingSocketChannel(channel);
        blockingChannel.configureBlocking(false);
        final TestChannelHandler testChannelHandler = new TestChannelHandler();
        cacheStopDispatcher.registerChannel(blockingChannel, testChannelHandler);
        testTarget.write(testString.getBytes());
        cacheStopDispatcher.waitForHandlerAdapter();
        final ChannelWriter channelWriter = testChannelHandler.getChannelWriter();
        Thread testThread = new Thread() {

            @Override
            public void run() {
                try {
                    StringToByteBufferTransformer transformer = new StringToByteBufferTransformer();
                    transformer.setNextForwarder(channelWriter);
                    transformer.forward("this MUST BLOCK right now!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        testThread.start();
        testThread.join(1000);
        assertFalse(testThread.isAlive());
        Field bufferField = ChannelWriter.class.getDeclaredField("buffer");
        bufferField.setAccessible(true);
        ByteBuffer buffer = (ByteBuffer) bufferField.get(channelWriter);
        assertTrue(buffer.hasRemaining());
    }
