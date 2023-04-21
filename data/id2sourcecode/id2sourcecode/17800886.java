    public void testIsSameBits() {
        Random rand = new Random(0L);
        for (int size = 0; size < 100; size++) {
            int[] ints = new int[(size + 7) / 8];
            IntArrayBitWriter w = new IntArrayBitWriter(ints, size);
            for (int i = 0; i < size; i++) w.writeBoolean(rand.nextBoolean());
            IntArrayBitReader r = new IntArrayBitReader(ints, size);
            IntArrayBitReader s = new IntArrayBitReader(ints, size);
            assertTrue(BitStreams.isSameBits(r, s));
            r.setPosition(0);
            s.setPosition(0);
            assertTrue(BitStreams.isSameBits(r, s));
            if (size > 0) {
                s.setPosition(0);
                int[] tints = ints.clone();
                int bit = rand.nextInt(size);
                int index = bit >> 5;
                int mask = 1 << (31 - (bit & 31));
                tints[index] ^= mask;
                IntArrayBitReader t = new IntArrayBitReader(tints, size);
                assertFalse(BitStreams.isSameBits(s, t));
                s.setPosition(0);
                t.setPosition(0);
                assertFalse(BitStreams.isSameBits(t, s));
                if (size > 2) {
                    int sub = 1 + rand.nextInt(size - 1);
                    s.setPosition(0);
                    t = new IntArrayBitReader(ints, size - sub);
                    assertFalse(BitStreams.isSameBits(s, t));
                    s.setPosition(0);
                    t.setPosition(0);
                    assertFalse(BitStreams.isSameBits(t, s));
                }
            }
        }
    }
