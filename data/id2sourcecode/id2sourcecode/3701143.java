    @Override
    public void setSignalTime(double time) {
        System.out.println("set " + xTimeRec + "to " + (time + relDelay));
        System.out.println("set " + yTimeRec + "to " + (time + relDelay));
        if (!caputFlag) {
            return;
        }
        xTimeCh = ChannelFactory.defaultFactory().getChannel(xTimeRec);
        yTimeCh = ChannelFactory.defaultFactory().getChannel(yTimeRec);
        CaMonitorScalar.setChannel(xTimeCh, time + relDelay);
        CaMonitorScalar.setChannel(yTimeCh, time + relDelay);
    }
