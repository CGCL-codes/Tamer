    public void finished(Channel realChannel, final int id) {
        ChannelsRunnable r = new ChannelsRunnable() {

            public void run() {
                try {
                    handler.finished(channel, id);
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
