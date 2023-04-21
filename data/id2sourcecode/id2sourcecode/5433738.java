    public void testDouble() {
        for (C coding : getCodings()) {
            if (isEncodableValueLimited(coding)) return;
            int bytes = 16;
            int[] memory = new int[bytes];
            IntArrayBitWriter writer = new IntArrayBitWriter(memory, bytes * 8);
            IntArrayBitReader reader = new IntArrayBitReader(memory, bytes * 8);
            checkDouble(writer, reader, 0.0);
            checkDouble(writer, reader, -0.0);
            checkDouble(writer, reader, 1.0);
            checkDouble(writer, reader, 2.0);
            checkDouble(writer, reader, 3.0);
            checkDouble(writer, reader, 4.0);
            for (double d = -100.0; d < 100.0; d += 0.1) {
                checkDouble(writer, reader, d);
            }
            Random r = new Random(0L);
            for (int i = 0; i < 10000; i++) {
                double d = Double.longBitsToDouble(r.nextLong());
                if (Double.isNaN(d) || Double.isInfinite(d)) continue;
                checkDouble(writer, reader, d);
            }
        }
    }
