    public void connected(final TCPChannel channel) throws IOException {
        ChannelsRunnable r = new ChannelsRunnable() {

            public void run() {
                try {
                    TCPChannel newChannel = new ThdTCPChannel(channel, svc, bufFactory);
                    cb.connected(newChannel);
                } catch (Exception e) {
                    log.log(Level.WARNING, channel + "Exception", e);
                }
            }

            public RegisterableChannel getChannel() {
                return svrChannel;
            }
        };
        svc.execute(channel, r);
    }
