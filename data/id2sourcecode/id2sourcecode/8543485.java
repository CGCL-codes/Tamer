    public int indexOfChannel(String channelName) {
        for (int i = 0; i < channels.size(); i++) {
            if (getChannel(i).getName().equals(channelName)) {
                return i;
            }
        }
        return -1;
    }
