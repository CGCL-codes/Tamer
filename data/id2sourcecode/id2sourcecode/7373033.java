    public Channel[] getChannels() {
        Channel[] chans = channels.values().toArray(new Channel[channels.size()]);
        Channel[] result = new Channel[chans.length];
        for (int i = 0; i < result.length; i++) result[i] = new Channel(chans[i].address, scaledValue(chans[i].value));
        return result;
    }
