    public void clockCheck(double time) {
        for (int i = 0; i < channels.length; i++) {
            channels[i].getChannelBeat().clockCheck(time);
        }
    }
