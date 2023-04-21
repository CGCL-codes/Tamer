    public void scratchKill() {
        for (int i = 0; i < channels.length; i++) {
            channels[i].getChannelBeat().kill();
        }
    }
