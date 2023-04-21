    private void setBGTimeInSample(int sample) {
        System.out.println("set " + bgTimeRec + "to " + sample);
        if (!caputFlag) {
            return;
        }
        bgTimeCh = ChannelFactory.defaultFactory().getChannel(bgTimeRec);
        CaMonitorScalar.setChannel(bgTimeCh, sample);
    }
