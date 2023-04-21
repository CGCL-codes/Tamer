    private int findTokenIndex(int off) {
        int a = 0;
        int b = nodeTokens.length;
        int idx = 0;
        while (a < b) {
            idx = (a + b) / 2;
            int bo = (idx > 0) ? nodeTokens[idx - 1].endOffset : 0;
            int eo = nodeTokens[idx].endOffset;
            if (off < bo) b = idx; else if (off > eo) a = idx + 1; else break;
        }
        return idx;
    }
