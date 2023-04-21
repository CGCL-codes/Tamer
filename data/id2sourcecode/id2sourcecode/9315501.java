    public void failure(Channel realChannel, final ByteBuffer data, final Exception ee) {
        ChannelsRunnable r = new ChannelsRunnable() {

            public void run() {
                try {
                    handler.failure(channel, data, ee);
                } catch (Exception e) {
                    log.log(Level.WARNING, channel + "Exception", e);
                }
            }

            public RegisterableChannel getChannel() {
                return channel;
            }
        };
        svc.execute(r);
    }
