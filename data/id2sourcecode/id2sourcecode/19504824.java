    private void correlate(BeamAnalyzerBean babean) {
        if (getChannelCorrelator() == null) {
            setChannelCorrelator(new OrbitDisplayChannelCorrelator(1.e7));
            getChannelCorrelator().setBeamAnalyzerBean(babean);
            getChannelCorrelator().addBPMs(babean.getSequenceBPMList());
            getChannelCorrelator().startMonitoring();
            getChannelCorrelator().addListener(getChannelCorrelator().getCorrelationListner());
            reportOnConnection(babean);
        } else {
            getChannelCorrelator().stopMonitoring();
            getChannelCorrelator().removeAllChannels();
            getChannelCorrelator().removeListener(getChannelCorrelator().getCorrelationListner());
            getChannelCorrelator().setBeamAnalyzerBean(babean);
            getChannelCorrelator().addBPMs(babean.getSequenceBPMList());
            getChannelCorrelator().startMonitoring();
            getChannelCorrelator().addListener(getChannelCorrelator().getCorrelationListner());
            reportOnConnection(babean);
        }
    }
