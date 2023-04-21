    public Channel[] getChannels() {
        Channel[] channels = cue.getChannels();
        Channel[] results = new Channel[channels.length];
        for (int i = 0; i < channels.length; i++) results[i] = new Channel(channels[i].address, (short) (channels[i].value * fadeLevel + 0.5));
        return results;
    }
