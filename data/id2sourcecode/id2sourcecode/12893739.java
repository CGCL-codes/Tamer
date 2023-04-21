    private Channel getChannel(ArrayList<Channel> channels, int pos, boolean hor_) {
        Channel channel;
        while (pos >= channels.size()) {
            channel = new Channel(hor_, channels.size());
            channels.add(channel);
        }
        channel = (Channel) channels.get(pos);
        return channel;
    }
