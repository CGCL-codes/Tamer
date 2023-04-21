    @Override
    public void stateChangedNNJImplAllChannels(NNJChangeEvent evt) {
        super.stateChangedNNJImplAllChannels(evt);
        hilbPhBuff = new double[this.getDataLayout().getChannelCount()][];
    }
