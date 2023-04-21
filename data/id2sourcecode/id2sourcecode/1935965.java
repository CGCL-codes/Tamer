    public int[] getAllChannelSources() {
        int[] values = new int[maxChannel];
        for (int i = 0; i < maxChannel; i++) values[i] = getChannelSource((short) (i + 1));
        return values;
    }
