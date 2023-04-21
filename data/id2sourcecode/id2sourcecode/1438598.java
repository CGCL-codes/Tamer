    public Extension getDefaultExtension(String channelName) {
        Channel channel = RBNBController.getInstance().getChannel(channelName);
        String mime = null;
        if (channel != null) {
            mime = channel.getMetadata("mime");
        }
        return findExtension(mime);
    }
