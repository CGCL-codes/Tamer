    public boolean write(OrderedCollectionBYTE ocbIn, long lOffsetIn, long lLengthIn) {
        int iSize;
        int iPage;
        int iPageElement;
        preparePageForOffset(mlCurrentOffset + lLengthIn - 1);
        iPage = getPageIndex(mlCurrentOffset);
        if (!ocbIn.seek(lOffsetIn)) {
            error("write(ocb,long,long) error seeking input ordered " + "collection offset=" + lOffsetIn);
            return (false);
        }
        for (; lLengthIn > 0; iPage++) {
            iPageElement = getPageElementIndex(mlCurrentOffset);
            iSize = maabPages[iPage].length - iPageElement;
            if (iSize > lLengthIn) {
                iSize = (int) lLengthIn;
            }
            if (DEBUG) debug("write(ocb,long,long) writing " + iSize + " elements on page " + iPage);
            if (!ocbIn.read(maabPages[iPage], iPageElement, iSize)) {
                error("write(ocb,long,long) error reading input ordered " + "collection offset=" + lOffsetIn + " length=" + iSize + ", returning");
                return (false);
            }
            lOffsetIn += iSize;
            lLengthIn -= iSize;
            mlCurrentOffset += iSize;
            if (DEBUG) debug("write(ocb,long,long) after writing lOffsetIn=" + lOffsetIn + " lLengthIn=" + lLengthIn + " mlCurrentOffset=" + mlCurrentOffset);
        }
        if (mlCurrentOffset > mlCurrentSize) {
            mlCurrentSize = mlCurrentOffset;
        }
        if (DEBUG) debug("write(ocb,long,long) finished, mlCurrentSize=" + mlCurrentSize);
        return (true);
    }
