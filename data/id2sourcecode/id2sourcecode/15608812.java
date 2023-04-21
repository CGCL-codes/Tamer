    protected final void initPages(int[] src, int addr, boolean ro) {
        int pageWords = (1 << pageShift) >>> 2;
        int pageMask = (1 << pageShift) - 1;
        for (int i = 0; i < src.length; ) {
            int page = addr >>> pageShift;
            int start = (addr & pageMask) >> 2;
            int elements = min(pageWords - start, src.length - i);
            if (readPages[page] == null) {
                initPage(page, ro);
            } else if (!ro) {
                if (writePages[page] == null) writePages[page] = readPages[page];
            }
            System.arraycopy(src, i, readPages[page], start, elements);
            i += elements;
            addr += elements * 4;
        }
    }
