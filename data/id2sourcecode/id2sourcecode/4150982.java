    protected void sendMessagesToChannel(final String channelName, int numMessages) throws Exception {
        for (int i = 0; i < numMessages; i++) {
            final MessageBuffer buf = (new MessageBuffer(4)).putInt(i);
            System.err.println("Sending message: " + HexDumper.format(buf.getBuffer()));
            txnScheduler.runTask(new TestAbstractKernelRunnable() {

                public void run() {
                    Channel channel = getChannel(channelName);
                    channel.send(null, ByteBuffer.wrap(buf.getBuffer()));
                }
            }, taskOwner);
        }
    }
