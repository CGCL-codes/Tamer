    private int indexOf(Channel channel, List<Channel> channels) {
        int index = -1;
        for (int i = 0; i < channels.size(); i++) {
            if (channel.getChannelURL().equals(channels.get(i).getChannelURL())) {
                return i;
            }
        }
        return index;
    }
