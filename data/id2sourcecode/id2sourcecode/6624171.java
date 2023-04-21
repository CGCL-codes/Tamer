    private boolean imageHasThumbnail() {
        if (channels.size() == 0) {
            return false;
        }
        String channelName = (String) channels.iterator().next();
        String thumbnailChannelName = THUMBNAIL_PLUGIN_NAME + "/" + channelName;
        Channel thumbnailChannel = rbnbController.getChannel(thumbnailChannelName);
        return thumbnailChannel != null;
    }
