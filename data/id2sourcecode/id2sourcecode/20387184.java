    private void testListIterator(BitVector v) {
        int size = v.size();
        final BitVector w = new BitVector(size);
        ListIterator<Boolean> i = v.listIterator();
        while (i.hasNext()) {
            w.setBit(i.nextIndex(), i.next());
        }
        assertEquals(v, w);
        final BitVector x = new BitVector(size);
        i = v.listIterator(size);
        while (i.hasPrevious()) {
            x.setBit(i.previousIndex(), i.previous());
        }
        assertEquals(v, x);
        final int a = random.nextInt(size + 1);
        i = v.listIterator(a);
        if (a == size) {
            assertEquals(-1, i.nextIndex());
        } else {
            assertEquals(a, i.nextIndex());
            assertEquals(v.getBit(a), i.next().booleanValue());
        }
        i = v.listIterator(a);
        if (a == 0) {
            assertEquals(-1, i.previousIndex());
        } else {
            assertEquals(a - 1, i.previousIndex());
            assertEquals(v.getBit(a - 1), i.previous().booleanValue());
        }
    }
