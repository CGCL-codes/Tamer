    @Test
    public void testChannelWriteReadObject() throws Exception {
        txnScheduler.runTask(new TestAbstractKernelRunnable() {

            public void run() throws Exception {
                Channel savedChannel = channelService.createChannel("x", null, Delivery.RELIABLE);
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bout);
                out.writeObject(savedChannel);
                out.flush();
                out.close();
                ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
                ObjectInputStream in = new ObjectInputStream(bin);
                Channel channel = (Channel) in.readObject();
                if (!savedChannel.equals(channel)) {
                    fail("Expected channel: " + savedChannel + ", got " + channel);
                }
                System.err.println("Channel {write,read}Object successful");
            }
        }, taskOwner);
    }
