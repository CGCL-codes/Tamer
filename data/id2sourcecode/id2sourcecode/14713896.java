    protected void removePair(BPMPair pair) {
        if (pvNames.contains(pair.getBPM1Name())) {
            pvNames.remove(pair.getBPM1Name());
            theCorrelator.removeChannel(pair.getChannel1());
        }
        if (!pvNames.contains(pair.getBPM2Name())) {
            pvNames.remove(pair.getBPM2Name());
            theCorrelator.removeChannel(pair.getChannel2());
        }
    }
