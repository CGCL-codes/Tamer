    protected void doSingle(int maxSize) {
        int sz = rand.nextInt(maxSize + 1);
        int sz2 = rand.nextInt(maxSize);
        OpenBitSet bs1 = getRandomSet(sz, rand.nextInt(sz + 1));
        OpenBitSet bs2 = getRandomSet(sz, rand.nextInt(sz2 + 1));
        DocSet a1 = new BitDocSet(bs1);
        DocSet a2 = new BitDocSet(bs2);
        DocSet b1 = getDocSet(bs1);
        DocSet b2 = getDocSet(bs2);
        checkEqual(bs1, b1);
        checkEqual(bs2, b2);
        iter(a1, b1);
        iter(a2, b2);
        OpenBitSet a_and = (OpenBitSet) bs1.clone();
        a_and.and(bs2);
        OpenBitSet a_or = (OpenBitSet) bs1.clone();
        a_or.or(bs2);
        OpenBitSet a_andn = (OpenBitSet) bs1.clone();
        a_andn.andNot(bs2);
        checkEqual(a_and, b1.intersection(b2));
        checkEqual(a_or, b1.union(b2));
        checkEqual(a_andn, b1.andNot(b2));
        assertEquals(a_and.cardinality(), b1.intersectionSize(b2));
        assertEquals(a_or.cardinality(), b1.unionSize(b2));
        assertEquals(a_andn.cardinality(), b1.andNotSize(b2));
    }
