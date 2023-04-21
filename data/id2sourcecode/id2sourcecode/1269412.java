    public int getChannelDepth(int channelIndex) {
        if (channelIndex == 0) {
            int l = cpm.getArray().length;
            for (int i = 0; ; i++) {
                if ((l >>= 1) == 0) {
                    return i;
                }
            }
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }
