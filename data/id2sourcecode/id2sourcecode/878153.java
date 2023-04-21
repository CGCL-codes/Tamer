    public void paint(Graphics g) {
        if (!running) return;
        int channels = 4 * asap.getInfo().getChannels();
        int channelWidth = getWidth() / channels;
        int totalHeight = getHeight();
        int unitHeight = totalHeight / 15;
        for (int i = 0; i < channels; i++) {
            int height = asap.getPokeyChannelVolume(i) * unitHeight;
            g.setColor(background);
            g.fillRect(i * channelWidth, 0, channelWidth, totalHeight - height);
            g.setColor(foreground);
            g.fillRect(i * channelWidth, totalHeight - height, channelWidth, height);
        }
    }
