    public static int getChannelType(String channelType) {
        for (int i = 0; i < CHANNELTYPES.length; i++) {
            if (CHANNELTYPES[i].equals(channelType)) return i;
        }
        return -1;
    }
