    @Test(timeout = 60 * 1000)
    public void testReSubmit() throws Exception {
        for (Executive e : execs) {
            e.start();
        }
        MockBlockingCallable.counter = 0;
        Future<?> f = client.submit(blockingCallable);
        while (MockBlockingCallable.counter == 0) {
            Thread.yield();
        }
        for (Executive e : execs) {
            e.getChannel().disconnect();
        }
        Executive e = new Executive();
        e.setClusterName(client.getClusterName());
        execs.add(e);
        e.start();
        MockBlockingCallable.counter = -2;
        assertEquals("Make sure the result was collected", MockBlockingCallable.class.toString(), f.get());
    }
