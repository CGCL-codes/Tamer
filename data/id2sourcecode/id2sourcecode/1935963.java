    public int[] getChannelSources(short[] addresses) {
        int[] values = new int[addresses.length];
        for (int i = 0; i < addresses.length; i++) values[i] = getChannelSource(addresses[i]);
        return values;
    }
