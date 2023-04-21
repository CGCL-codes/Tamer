    private void setSignalTimeInSample(int sample) {
        System.out.println("set " + sigTimeRec + "to " + sample);
        if (!caputFlag) {
            return;
        }
        sigTimeCh = ChannelFactory.defaultFactory().getChannel(sigTimeRec);
        CaMonitorScalar.setChannel(sigTimeCh, sample);
    }
