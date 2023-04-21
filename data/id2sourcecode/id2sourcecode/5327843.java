    public void setSignalTime(double time) {
        System.out.println("set " + sigTimeRec + "to " + time);
        if (!caputFlag) {
            return;
        }
        sigTimeCh = ChannelFactory.defaultFactory().getChannel(sigTimeRec);
        CaMonitorScalar.setChannel(sigTimeCh, time);
    }
