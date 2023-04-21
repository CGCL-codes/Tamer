    public short getChannelValue(short address) {
        return (short) (cue.getChannelValue(address) * fadeLevel + 0.5);
    }
