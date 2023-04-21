    public boolean hasMoreTokens() {
        if (readIndex == -1) return writeIndex > readIndex;
        return buf[getIndex(readIndex)] != 65535;
    }
