    public boolean getChannelPolarity(int channel, byte[] register) {
        byte polarity = (byte) (0x01 << channel);
        return ((register[1] & polarity) == polarity);
    }
