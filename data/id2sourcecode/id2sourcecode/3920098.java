    private long seekPosition(long key) throws IOException {
        long pos = -1;
        long low = 0;
        long high = bookFile.length() / 16;
        Long currentKey = null;
        while (1 < high - low) {
            pos = (high + low) / 2;
            currentKey = readKey(pos);
            if (currentKey == key) {
                break;
            } else if (isLessThanUnsigned(currentKey, key)) {
                low = pos;
            } else {
                high = pos;
            }
        }
        if (currentKey != key) {
            pos = low;
            currentKey = readKey(pos);
        }
        if (currentKey != key) {
            pos = high;
            currentKey = readKey(pos);
        }
        if (currentKey == key) {
            return pos;
        }
        return -1;
    }
