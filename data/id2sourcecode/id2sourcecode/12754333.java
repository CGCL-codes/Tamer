    void connectToChannels() {
        int[] channels = this.player.getContent().channels;
        Channel chan;
        for (int c : channels) {
            if (c > 0) {
                chan = Channel.getChannel(c);
                chan.addClientInSilent(this);
            }
        }
    }
