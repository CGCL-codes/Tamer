    public static String getURL(int opCode) {
        switch(opCode) {
            case OP_GET_CHANNEL_LIST:
                return getChannelListURL();
            default:
                return "";
        }
    }
