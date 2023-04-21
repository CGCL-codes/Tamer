    public void makeSilence() {
        if (getChannelCount() > 0) {
            makeSilence(0);
            for (int ch = 1; ch < getChannelCount(); ch++) {
                copyChannel(0, ch);
            }
        }
    }
