    public static int getChannelResolution() {
        getItems();
        return Utils.getInt(general, "rayprocessor.channelresolution", DEFAULT_CHANNELRESOLUTION);
    }
