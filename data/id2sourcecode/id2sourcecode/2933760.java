    public void shiftAllChannelsDelayUp() {
        hypeIndex++;
        for (GenericChannel ch : channels) {
            ch.getChannelBeat().setGearIndex(ch.getInitDelayIndex() + hypeIndex, false);
        }
        ChannelFrame.controlPanel.delayPanel.loadValue();
    }
