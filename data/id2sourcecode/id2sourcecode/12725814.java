    private int getChannelIndex() {
        return (getAC3mod() >> 5) & 0x07;
    }
