    private void setBGRangeInSample(int sample) {
        System.out.println("set " + bgRangeRec + "to " + sample);
        if (!caputFlag) {
            return;
        }
        bgRangeCh = ChannelFactory.defaultFactory().getChannel(bgRangeRec);
        CaMonitorScalar.setChannel(bgRangeCh, sample);
    }
