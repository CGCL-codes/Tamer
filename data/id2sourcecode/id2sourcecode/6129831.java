    public static String createChannel(Key key) {
        return getChannelService().createChannel("" + key.getId());
    }
