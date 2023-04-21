    @Override
    protected int getChannelCount() {
        FluidsynthSound sound = getElement();
        return sound.getChannels();
    }
