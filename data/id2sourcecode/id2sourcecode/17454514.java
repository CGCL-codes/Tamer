    public void run() {
        Channel channel = ChannelFactory.defaultFactory().getChannel(ch);
        channel.connectAndWait();
        final int count = 1024;
        System.out.println("count = " + count);
        try {
            channel.addMonitorValTime(this, Monitor.VALUE);
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (MonitorException e) {
            e.printStackTrace();
        }
    }
