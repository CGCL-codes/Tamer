    private boolean isChannelSupported(String channelName) {
        Channel channel = rbnbController.getChannel(channelName);
        if (channel == null) {
            return false;
        }
        String mimeType = channel.getMetadata("mime");
        Extension extension = dataPanelManager.getExtension(this.getClass());
        if (extension != null) {
            List<String> mimeTypes = extension.getMimeTypes();
            for (int i = 0; i < mimeTypes.size(); i++) {
                String mime = mimeTypes.get(i);
                if (mime.equals(mimeType)) {
                    return true;
                }
            }
        }
        return false;
    }
