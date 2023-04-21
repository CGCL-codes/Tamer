    public void setBGTime(double time) {
        System.out.println("set " + bgTimeRec + "to " + time);
        if (!caputFlag) {
            return;
        }
        bgTimeCh = ChannelFactory.defaultFactory().getChannel(bgTimeRec);
        CaMonitorScalar.setChannel(bgTimeCh, time);
    }
