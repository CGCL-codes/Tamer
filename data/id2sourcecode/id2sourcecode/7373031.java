    public short getChannelValue(short address) {
        Channel chan = channels.get(address);
        if (chan != null) return scaledValue(chan.value); else return (short) 0;
    }
