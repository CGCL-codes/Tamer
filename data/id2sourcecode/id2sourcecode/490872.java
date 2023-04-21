    private int recFind(long searchKey, int lowerBound, int upperBound) {
        int curIn;
        curIn = (lowerBound + upperBound) / 2;
        if (a[curIn] == searchKey) return curIn; else if (lowerBound > upperBound) return nElems; else {
            if (a[curIn] < searchKey) return recFind(searchKey, curIn + 1, upperBound); else return recFind(searchKey, lowerBound, curIn - 1);
        }
    }
