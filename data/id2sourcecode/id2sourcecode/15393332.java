    private void setSignalRangeInSample(int sample) {
        System.out.println("set " + xRangeRec + "to " + sample);
        System.out.println("set " + yRangeRec + "to " + sample);
        if (!caputFlag) {
            return;
        }
        xRangeCh = ChannelFactory.defaultFactory().getChannel(xRangeRec);
        yRangeCh = ChannelFactory.defaultFactory().getChannel(yRangeRec);
        CaMonitorScalar.setChannel(xRangeCh, sample);
        CaMonitorScalar.setChannel(yRangeCh, sample);
    }
