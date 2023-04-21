    private synchronized void setup() {
        Channel channel = ChannelFactory.defaultFactory().getChannel(ch);
        channel.connectAndWait(caTimeOut);
        try {
            channel.addMonitorValTime(this, Monitor.VALUE);
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (MonitorException e) {
            e.printStackTrace();
        }
    }
