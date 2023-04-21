    public void clearIntensity() {
        for (int i = 0; i < getNumberOfChannelSelections(); i++) {
            getChannelSelection(i).clearIntensity();
        }
    }
