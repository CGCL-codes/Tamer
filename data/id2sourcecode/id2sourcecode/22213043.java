    private void consumeByte(IOType io, int raw) {
        RecorderChannel channel = getChannelByName(io.getPublicName());
        if (channel != null) {
            channel.append(raw);
        }
    }
