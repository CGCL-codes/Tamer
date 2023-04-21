    public void sendChannelMessage(int player, String channel, Object message) {
        try {
            getChannel(player, channel).send(toByteBuffer(message));
        } catch (IOException e) {
            throw new HotspotIOException("Error sending message to channel", e);
        }
    }
