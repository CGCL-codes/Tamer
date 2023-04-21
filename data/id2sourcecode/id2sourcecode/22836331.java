    public boolean[] getChannels() {
        boolean channels[] = new boolean[4];
        for (int i = 0; i < this.channels.length; i++) channels[this.channels[i]] = true;
        return channels;
    }
