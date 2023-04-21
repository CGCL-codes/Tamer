    public void incomingData(Channel realChannel, ByteBuffer b) throws IOException {
        int length = b.remaining();
        final ByteBuffer newBuffer = bufFactory.createBuffer(channel, length);
        newBuffer.put(b);
        ChannelsRunnable r = new ChannelsRunnable() {

            public void run() {
                try {
                    HELPER.doneFillingBuffer(newBuffer);
                    handler.incomingData(channel, newBuffer);
                } catch (Exception e) {
                    log.log(Level.WARNING, channel + "Exception", e);
                }
            }

            public RegisterableChannel getChannel() {
                return channel;
            }
        };
        svc.execute(realChannel, r);
    }
