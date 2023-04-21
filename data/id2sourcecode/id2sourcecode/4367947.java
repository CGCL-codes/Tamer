    public void testVerySmallReadWrite() throws Exception {
        ByteBuffer b = ByteBuffer.allocate(4000);
        log.info("getting all proper connections");
        int size = 40;
        String[] methodNames = new String[size];
        for (int i = 0; i < size; i++) {
            methodNames[i] = "connected";
        }
        TCPChannel[] clients = new TCPChannel[size];
        for (int i = 0; i < size; i++) {
            clients[i] = chanMgr.createTCPChannel("Client[" + i + "]", getClientFactoryHolder());
            clients[i].connect(svrAddr, (ConnectionCallback) mockConnect);
        }
        mockConnect.expect(methodNames);
        log.info("done getting all connections");
        for (TCPChannel client : clients) {
            client.registerForReads((DataListener) mockHandler);
        }
        int numWrites = 200;
        String payload = "hello";
        helper.putString(b, payload);
        helper.doneFillingBuffer(b);
        int numBytes = b.remaining();
        methodNames = new String[size * numWrites];
        for (int i = 0; i < size * numWrites; i++) {
            methodNames[i] = "incomingData";
        }
        PerfTimer timer = new PerfTimer();
        PerfTimer timer2 = new PerfTimer();
        timer.start();
        timer2.start();
        for (TCPChannel client : clients) {
            for (int i = 0; i < numWrites; i++) {
                client.write(b);
                b.rewind();
            }
        }
        long result2 = timer2.stop();
        CalledMethod[] methods = mockHandler.expect(methodNames);
        long result = timer.stop();
        ByteBuffer actualBuf = (ByteBuffer) methods[5].getAllParams()[1];
        String actual = helper.readString(actualBuf, actualBuf.remaining());
        assertEquals(payload, actual);
        log.info("payload=" + actual);
        long readWriteTime = result / size;
        long byteTime = 100 * result / (numWrites * numBytes);
        log.info("total write time         =" + result2);
        log.info("total write/read time    =" + result);
        log.info("--time per 100 bytes     =" + byteTime);
        log.info("test result info:");
        log.info("--time per write/read    =" + readWriteTime);
        log.info("  time to beat           =" + getSmallReadWriteTimeLimit());
        assertTrue(readWriteTime < getSmallReadWriteTimeLimit());
    }
