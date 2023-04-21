    @Override
    public void setSignalRange(double time) {
        System.out.println("set " + xRangeRec + "to " + time);
        System.out.println("set " + yRangeRec + "to " + time);
        if (!caputFlag) {
            return;
        }
        xRangeCh = ChannelFactory.defaultFactory().getChannel(xRangeRec);
        yRangeCh = ChannelFactory.defaultFactory().getChannel(yRangeRec);
        CaMonitorScalar.setChannel(xRangeCh, time);
        CaMonitorScalar.setChannel(yRangeCh, time);
    }
