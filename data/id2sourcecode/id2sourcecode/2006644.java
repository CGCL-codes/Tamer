    private static String getReplacement(char c) throws IOException {
        int hi = lookupIndexLen - 1, lo = 0;
        int mid;
        char curBeg, curEnd;
        int offset = -1;
        byte len = 0;
        InputStream data;
        byte[] replBytes;
        while (lo <= hi) {
            mid = (hi + lo) / 2;
            curBeg = (char) ((lookupIndex[mid * LOOKUP_ENTRY_SIZE] << 8) | (0xFF & lookupIndex[mid * LOOKUP_ENTRY_SIZE + 1]));
            curEnd = (char) ((lookupIndex[mid * LOOKUP_ENTRY_SIZE + 2] << 8) | (0xFF & lookupIndex[mid * LOOKUP_ENTRY_SIZE + 3]));
            if (c < curBeg) {
                hi = mid - 1;
            } else if (c > curEnd) {
                lo = mid + 1;
            } else {
                len = lookupIndex[mid * LOOKUP_ENTRY_SIZE + 4];
                offset = (lookupIndex[mid * LOOKUP_ENTRY_SIZE + 5] << 24) | (0xFF0000 & (lookupIndex[mid * LOOKUP_ENTRY_SIZE + 6] << 16)) | (0xFF00 & (lookupIndex[mid * LOOKUP_ENTRY_SIZE + 7] << 8)) | (0xFF & lookupIndex[mid * LOOKUP_ENTRY_SIZE + 8]);
                offset += len * (c - curBeg);
                break;
            }
        }
        if (offset == -1) {
            return null;
        }
        data = Normalizer.class.getResourceAsStream(REPL_TABLE_FILE);
        data.skip(offset);
        data.read(replBytes = new byte[len]);
        return new String(replBytes, "UTF8");
    }
