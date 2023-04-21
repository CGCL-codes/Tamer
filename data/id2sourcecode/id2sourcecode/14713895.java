    protected void addPair(BPMPair pair) {
        if (!pvNames.contains(pair.getBPM1Name())) {
            pvNames.add(pair.getBPM1Name());
            theCorrelator.addChannel(pair.getChannel1());
        }
        if (!pvNames.contains(pair.getBPM2Name())) {
            pvNames.add(pair.getBPM2Name());
            theCorrelator.addChannel(pair.getChannel2());
        }
    }
