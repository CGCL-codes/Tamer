    @Test
    public void testWriteUsingSocketTransport() throws Exception {
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
        acceptor.setReuseAddress(true);
        SocketAddress address = new InetSocketAddress("localhost", AvailablePortFinder.getNextAvailable());
        NioSocketConnector connector = new NioSocketConnector();
        byte[] data = new byte[4 * 1024 * 1024];
        new Random().nextBytes(data);
        byte[] expectedMd5 = MessageDigest.getInstance("MD5").digest(data);
        M message = createMessage(data);
        SenderHandler sender = new SenderHandler(message);
        ReceiverHandler receiver = new ReceiverHandler(data.length);
        acceptor.setHandler(sender);
        connector.setHandler(receiver);
        acceptor.bind(address);
        connector.connect(address);
        sender.latch.await();
        receiver.latch.await();
        acceptor.dispose();
        assertEquals(data.length, receiver.bytesRead);
        byte[] actualMd5 = receiver.digest.digest();
        assertEquals(expectedMd5.length, actualMd5.length);
        for (int i = 0; i < expectedMd5.length; i++) {
            assertEquals(expectedMd5[i], actualMd5[i]);
        }
    }
