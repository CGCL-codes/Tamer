    protected final void clearPages(int addr, int words) {
        int pageWords = (1 << pageShift) >>> 2;
        int pageMask = (1 << pageShift) - 1;
        for (int i = 0; i < words; ) {
            int page = addr >>> pageShift;
            int start = (addr & pageMask) >> 2;
            int elements = min(pageWords - start, words - i);
            if (readPages[page] == null) {
                readPages[page] = writePages[page] = new int[pageWords];
            } else {
                if (writePages[page] == null) writePages[page] = readPages[page];
                for (int j = start; j < start + elements; j++) writePages[page][j] = 0;
            }
            i += elements;
            addr += elements * 4;
        }
    }
