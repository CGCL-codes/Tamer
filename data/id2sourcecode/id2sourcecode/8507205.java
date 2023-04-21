    @Override
    public void setSignalRange(double time) {
        System.out.println("set " + sigRangeRec + "to " + time);
        if (!caputFlag) {
            return;
        }
        sigRangeCh = ChannelFactory.defaultFactory().getChannel(sigRangeRec);
        CaMonitorScalar.setChannel(sigRangeCh, time);
    }
