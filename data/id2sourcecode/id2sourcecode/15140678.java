    public void testTail() throws Exception {
        PipedWriter writer = new PipedWriter();
        Reader reader = new PipedReader(writer);
        writer.write("test\n");
        TailMonitor tail = new TailMonitor("TailMonitor", reader);
        StringListenerStub listener = new StringListenerStub();
        tail.subscribe(listener);
        final Server server = new DefaultServer();
        server.register(tail);
        server.start();
        synchronized (listener) {
            if (listener.getMessage() == null) listener.wait();
        }
        assertEquals("test", listener.getMessage());
        server.stop();
    }
