    public void connectFailed(RegisterableChannel channel, final Throwable e) {
        ChannelsRunnable r = new ChannelsRunnable() {

            public void run() {
                try {
                    cb.connectFailed(svrChannel, e);
                } catch (Exception e) {
                    log.log(Level.WARNING, svrChannel + "Exception", e);
                }
            }

            public RegisterableChannel getChannel() {
                return svrChannel;
            }
        };
        svc.execute(r);
    }
