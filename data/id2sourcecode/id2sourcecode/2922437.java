    public void init() {
        channel = ChannelFactory.defaultFactory().getChannel(ch);
        channel.connectAndWait(1000);
        try {
            channel.addMonitorValTime(this, Monitor.VALUE);
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (MonitorException e) {
            e.printStackTrace();
        }
    }
