    public RecorderChannel getChannelByName(String publicName) {
        RecorderChannel channel = null;
        int cx = size();
        while (--cx >= 0) {
            channel = channels.get(cx);
            if (channel.getName().equals(publicName)) {
                break;
            }
        }
        return channel;
    }
