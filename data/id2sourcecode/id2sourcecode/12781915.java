    protected int skipOffTokenChannels(int i) {
        sync(i);
        while (tokens.get(i).getChannel() != channel) {
            i++;
            sync(i);
        }
        return i;
    }
