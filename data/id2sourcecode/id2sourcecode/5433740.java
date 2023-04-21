    public void testDecimal() {
        for (C coding : getCodings()) {
            if (isEncodableValueLimited(coding)) return;
            int bits = 10240;
            int[] memory = new int[bits / 8];
            IntArrayBitWriter writer = new IntArrayBitWriter(memory, bits);
            IntArrayBitReader reader = new IntArrayBitReader(memory, bits);
            Random r = new Random(0L);
            for (int i = 0; i < 10000; i++) {
                checkDecimal(writer, reader, new BigDecimal(new BigInteger(r.nextInt(bits / 4), r), r.nextInt(100) - 50));
            }
        }
    }
