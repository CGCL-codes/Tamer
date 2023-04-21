    public void failed(Channel c, final int id, final Throwable e) {
        ChannelsRunnable r = new ChannelsRunnable() {

            public void run() {
                try {
                    handler.failed(channel, id, e);
                } catch (Exception e) {
                    log.log(Level.WARNING, channel + "Exception", e);
                }
            }

            public RegisterableChannel getChannel() {
                return channel;
            }
        };
        svc.execute(c, r);
    }
