    public void addChannel(String name) {
        channelNames.add(name);
        for (Stream s : streams) {
            s.addChannel(SourceChannel.getChannel(name, s));
        }
    }
