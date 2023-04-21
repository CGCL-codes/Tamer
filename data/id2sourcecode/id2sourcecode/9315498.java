    public void farEndClosed(Channel realChannel) {
        ChannelsRunnable r = new ChannelsRunnable() {

            public void run() {
                try {
                    handler.farEndClosed(channel);
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
