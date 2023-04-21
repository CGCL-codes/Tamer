    @Override
    public void stateChangedNNJImplLayout(NNJChangeEvent evt) {
        if (inverted.length != getDataLayout().getChannelCount()) {
            inverted = new boolean[getDataLayout().getChannelCount()];
        }
    }
