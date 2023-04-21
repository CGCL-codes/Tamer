    public int[] getChannelSources(short startAddress, short endAddress) {
        int[] values = new int[endAddress - startAddress + 1];
        for (short i = startAddress; i <= endAddress; i++) values[i - startAddress] = getChannelSource(i);
        return values;
    }
