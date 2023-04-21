    public short[] getChannelValues(short startAddress, short endAddress) {
        short[] values = new short[endAddress - startAddress + 1];
        for (short i = startAddress; i <= endAddress; i++) values[i - startAddress] = getChannelValue(i);
        return values;
    }
