    public boolean getChannelMask(int channel, byte[] register) {
        byte mask = (byte) (0x01 << channel);
        return ((register[0] & mask) == mask);
    }
