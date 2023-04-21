    private void setSignalRangeInSample(int sample) {
        System.out.println("set " + sigRangeRec + "to " + sample);
        if (!caputFlag) {
            return;
        }
        sigRangeCh = ChannelFactory.defaultFactory().getChannel(sigRangeRec);
        CaMonitorScalar.setChannel(sigRangeCh, sample);
    }
