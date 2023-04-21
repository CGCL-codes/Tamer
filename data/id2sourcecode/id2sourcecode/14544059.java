    public void leaveAllChannels() {
        for (String name : this.channelMap.keySet()) {
            ChatChannelFrame frame = channelMap.get(name);
            leaveChannel(frame.getChannelName());
        }
    }
