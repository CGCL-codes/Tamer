    public WaveformView(Session doc, ComponentHost host) {
        super();
        this.host = host;
        final AudioTrail at = doc.getAudioTrail();
        fullChannels = at.getChannelNum();
        channelMap = new int[fullChannels];
        for (int i = 0; i < fullChannels; i++) {
            channelMap[i] = i;
        }
        this.doc = doc;
    }
