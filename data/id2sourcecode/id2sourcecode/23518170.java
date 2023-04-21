    public void checkMatch(boolean finalPass) {
        int max = aCount;
        if (max > bCount) max = bCount;
        int i;
        for (i = 0; i < max; ++i) {
            if (!a[i].equals(b[i])) break;
        }
        maxSame = i;
        aTop = bTop = maxSame;
        if (maxSame > 0) last = a[maxSame - 1];
        next = "";
        if (finalPass) {
            aTop = aCount;
            bTop = bCount;
            next = "";
            return;
        }
        if (aCount - maxSame < EQUALSIZE || bCount - maxSame < EQUALSIZE) return;
        int match = find(a, aCount - EQUALSIZE, aCount, b, maxSame, bCount);
        if (match != -1) {
            aTop = aCount - EQUALSIZE;
            bTop = match;
            next = a[aTop];
            return;
        }
        match = find(b, bCount - EQUALSIZE, bCount, a, maxSame, aCount);
        if (match != -1) {
            bTop = bCount - EQUALSIZE;
            aTop = match;
            next = b[bTop];
            return;
        }
        if (aCount >= STACKSIZE || bCount >= STACKSIZE) {
            aCount = (aCount + maxSame) / 2;
            bCount = (bCount + maxSame) / 2;
            next = "";
        }
    }
