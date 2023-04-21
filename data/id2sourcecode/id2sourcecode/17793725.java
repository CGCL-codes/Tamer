    public void setChannel(Channel channel) {
        if (channel == texChannel) return;
        texChannel = channel;
        allConnectionPoints.clear();
        outputConnectionPoint = new ConnectionPoint(this, width / 2 - 4, height - 8, -1, texChannel.getOutputType());
        allConnectionPoints.add(outputConnectionPoint);
        int x = 8;
        for (int i = 0; i < texChannel.getNumInputChannels(); i++) {
            allConnectionPoints.add(new ConnectionPoint(this, x, 0, i, texChannel.getChannelInputType(i)));
            x += 12;
        }
    }
