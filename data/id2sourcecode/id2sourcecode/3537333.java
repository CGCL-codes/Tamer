    public int getMedian() {
        int fsize = m_false.cardinality();
        int tsize = m_true.cardinality();
        if (fsize == 0 && tsize == 0) return Integer.MIN_VALUE;
        int med = (fsize + tsize) / 2;
        BitSet set = (fsize > tsize ? m_false : m_true);
        for (int i = set.nextSetBit(0), j = 0; i >= 0; i = set.nextSetBit(i + 1), ++j) {
            if (j == med) return i;
        }
        return Integer.MIN_VALUE;
    }
