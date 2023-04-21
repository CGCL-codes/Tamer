    public final void superMaskEdgeChannels(int ring, boolean triggerChangeEvent) {
        if (isMaskSuperimposer) {
            NNJDataLayout tempLayout = this.getDataLayout();
            for (int ch = 0; ch < tempLayout.getChannelCount(); ch++) {
                if (tempLayout.isEdge(ch, ring)) {
                    superMaskSetHideChannel(ch, false);
                }
            }
            if (triggerChangeEvent) {
                stateChangedNNJ(NNJChangeEvent.factoryMask());
            }
        }
    }
