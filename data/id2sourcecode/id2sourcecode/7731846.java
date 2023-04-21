    public Channel getChannel(String channelName) {
        if (channelName.equals("elements")) {
            return getElementsChannel();
        }
        if (channelName.equals("selection")) {
            return mySelectedElementChannel;
        }
        throw new IllegalArgumentException("Bad channelName: " + channelName);
    }
