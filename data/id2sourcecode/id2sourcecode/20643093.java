    public void testSetReadObserverGoesThroughChains() throws Exception {
        NIOSocket socket = new NIOSocket();
        Object channel = socket.getChannel();
        RCROAdapter entry = new RCROAdapter();
        socket.setReadObserver(entry);
        Thread.sleep(1000);
        assertInstanceof(SocketInterestReadAdapter.class, entry.getReadChannel());
        assertSame(channel, ((SocketInterestReadAdapter) entry.getReadChannel()).getChannel());
        RCRAdapter chain1 = new RCRAdapter();
        entry.setReadChannel(chain1);
        socket.setReadObserver(entry);
        Thread.sleep(1000);
        assertInstanceof(SocketInterestReadAdapter.class, chain1.getReadChannel());
        assertSame(channel, ((SocketInterestReadAdapter) chain1.getReadChannel()).getChannel());
        assertSame(chain1, entry.getReadChannel());
        RCRAdapter chain2 = new RCRAdapter();
        chain1.setReadChannel(chain2);
        socket.setReadObserver(entry);
        Thread.sleep(1000);
        assertInstanceof(SocketInterestReadAdapter.class, chain2.getReadChannel());
        assertSame(channel, ((SocketInterestReadAdapter) chain2.getReadChannel()).getChannel());
        assertSame(chain2, chain1.getReadChannel());
        assertSame(chain1, entry.getReadChannel());
    }
