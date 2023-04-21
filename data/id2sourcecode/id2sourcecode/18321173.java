    public void setBGRange(double time) {
        System.out.println("set " + bgRangeRec + "to " + time);
        if (!caputFlag) {
            return;
        }
        bgRangeCh = ChannelFactory.defaultFactory().getChannel(bgRangeRec);
        CaMonitorScalar.setChannel(bgRangeCh, time);
    }
