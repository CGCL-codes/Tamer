    public OutputStream createOutputStream(int streamId) {
        byte channelId = (byte) (4 + ((streamId - 1) * 5));
        final Channel data = getChannel(channelId++);
        final Channel video = getChannel(channelId++);
        final Channel audio = getChannel(channelId++);
        return new OutputStream(video, audio, data);
    }
