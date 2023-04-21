    public boolean offer(E elt) {
        if (mSize == mElts.length) {
            int c = mComparator.compare(mElts[mElts.length - 1], elt);
            if (c >= 0) return false;
            mElts[mElts.length - 1] = elt;
        }
        if (mSize < mElts.length) {
            mElts[mSize] = elt;
            ++mSize;
        }
        for (int i = mSize - 1; --i >= 0 && mComparator.compare(mElts[i], mElts[i + 1]) < 0; ) {
            E temp = mElts[i];
            mElts[i] = mElts[i + 1];
            mElts[i + 1] = temp;
        }
        return true;
    }
