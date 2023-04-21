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
            clients[i] = chanMgr.createTCPChannel("Client[" + i + "]", factoryHolder);
            log.fine("starting connect");
            Thread.sleep(100);
            clients[i].connect(delaySvrAddr, (ConnectionCallback) mockConnect);
        }
        mockConnect.expect(methodNames);
        log.info("done getting all connections");
        for (TCPChannel client : clients) {
            client.registerForReads((DataListener) mockHandler);
        }
        int numWrites = 200;
        String payload = "hello";
        HELPER.putString(b, payload);
        HELPER.doneFillingBuffer(b);
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
        mockHandler.setExpectTimeout(10000);
        long result2 = timer2.stop();
        CalledMethod[] methods = mockHandler.expect(methodNames);
        long result = timer.stop();
        ByteBuffer actualBuf = (ByteBuffer) methods[0].getAllParams()[1];
        String actual = HELPER.readString(actualBuf, actualBuf.remaining());
        assertEquals(payload, actual);
        log.info("payload=" + actual);
        long readWriteTime = result / size;
        log.info("total write time         =" + result2);
        log.info("total write/read time    =" + result);
        log.info("--time per write/read    =" + readWriteTime);
    }
