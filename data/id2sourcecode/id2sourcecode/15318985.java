    @Test
    @Ignore
    public void write() throws InterruptedException {
        assumingBasePackagesLoad();
        SocketWriteTestDriver writer = new SocketWriteTestDriver();
        new Thread(writer).start();
        synchronized (SocketWriteTestDriver.READY_FOR_CONNECTIONS) {
            SocketWriteTestDriver.READY_FOR_CONNECTIONS.wait();
        }
        eval("con <- socketConnection(port=" + port + ", host=\"localhost\")");
        System.out.println("Connected on client");
        System.out.println("Ready to write");
        System.out.println("Writing");
        eval("writeLines(\"foobar\",con)");
        synchronized (SocketWriteTestDriver.READY_FOR_READING) {
            SocketWriteTestDriver.READY_FOR_READING.wait();
        }
        assertThat(writer.getResult(), equalTo("foobar"));
    }
