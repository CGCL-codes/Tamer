    public void setInvertNonOptical(boolean value) {
        NNJChangeEvent tempEvt = NNJChangeEvent.factorySomeChannels();
        for (int det = getDataLayout().getDetectorCount(); det < this.getDataLayout().getChannelCount(); det++) {
            if (inverted[det] != value) {
                inverted[det] = value;
                tempEvt.setChangedChannels(det);
            }
        }
        if (tempEvt.getChangedChannelsCount() > 0) {
            stateChangedNNJ(tempEvt);
        }
    }
