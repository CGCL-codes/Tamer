    public void setInterval(double interval) {
        System.out.println("set digi " + id + " interval to " + interval);
        if (!caputFlag) {
            return;
        }
        daqOff();
        intervalSetCh = ChannelFactory.defaultFactory().getChannel(intervalSetRec);
        CaMonitorScalar.setChannel(intervalSetCh, interval);
        daqOn();
    }
