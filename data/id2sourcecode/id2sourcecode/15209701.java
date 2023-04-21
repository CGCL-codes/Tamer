    public PVInput(String s) {
        super(s);
        try {
            chan = channelFactory.getChannel(s);
            chan.connect();
            chan.addMonitorValTime(this, 1);
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (MonitorException e) {
            e.printStackTrace();
        }
    }
