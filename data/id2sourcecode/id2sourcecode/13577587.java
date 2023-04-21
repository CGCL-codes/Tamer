    public void setInvertAll(boolean value) {
        NNJChangeEvent tempEvt = NNJChangeEvent.factorySomeChannels();
        for (int det = 0; det < getDataLayout().getChannelCount(); det++) {
            if (inverted[det] != value) {
                inverted[det] = value;
                tempEvt.setChangedChannels(det);
            }
        }
        if (tempEvt.getChangedChannelsCount() > 0) {
            stateChangedNNJ(tempEvt);
        }
    }
