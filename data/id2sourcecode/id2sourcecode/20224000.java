    public void testTail() throws Exception {
        PipedWriter writer = new PipedWriter();
        Reader reader = new PipedReader(writer);
        writer.write("test\n");
        TailMonitor tail = new TailMonitor("TailMonitor", reader);
        MockStringListener listener = new MockStringListener();
        tail.subscribe(listener);
        final Server server = new DefaultServer();
        server.register(tail);
        server.start();
        synchronized (listener) {
            if (listener.getMessage() == null) listener.wait();
        }
        assertEquals("test", listener.getMessage());
        new Thread("New") {

            @Override
            public void run() {
                server.stop();
            }
        }.start();
    }
