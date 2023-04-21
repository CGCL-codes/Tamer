    public void testBigInt() {
        int bits = 4096;
        IntArrayBitWriter writer = new IntArrayBitWriter(bits);
        IntArrayBitReader reader = new IntArrayBitReader(writer.getInts(), writer.getSize());
        for (long i = 0; i < 100L; i++) {
            checkPositiveBigInt(writer, reader, BigInteger.valueOf(i));
        }
        for (long i = 0; i < 10000000000L; i += 1000000L) {
            checkPositiveBigInt(writer, reader, BigInteger.valueOf(i));
        }
        Random r = new Random(0L);
        for (int i = 0; i < 10000; i++) {
            BigInteger value = new BigInteger(r.nextInt(bits / 4), r);
            checkBigInt(writer, reader, value);
        }
    }
