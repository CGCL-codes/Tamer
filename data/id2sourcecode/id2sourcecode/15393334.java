    private void setSignalTimeInSample(int sample) {
        System.out.println("set " + xTimeRec + "to " + sample);
        System.out.println("set " + yTimeRec + "to " + sample);
        if (!caputFlag) {
            return;
        }
        xTimeCh = ChannelFactory.defaultFactory().getChannel(xTimeRec);
        yTimeCh = ChannelFactory.defaultFactory().getChannel(yTimeRec);
        CaMonitorScalar.setChannel(xTimeCh, sample);
        CaMonitorScalar.setChannel(yTimeCh, sample);
    }
