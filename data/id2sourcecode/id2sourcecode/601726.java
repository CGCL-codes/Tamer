    public Cursor(WaveformPanelContainer container, Plei plei) {
        this.container = container;
        this.plei = plei;
        position = 0;
        channelLengthInSamples = plei.getChannelLength();
        paused = true;
        stopped = false;
    }
