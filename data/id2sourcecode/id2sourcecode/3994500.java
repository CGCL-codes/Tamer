    private void testSpeed(int size, int bound) {
        for (ExtendedCoding coding : getCodings()) {
            int[] memory = new int[size];
            IntArrayBitWriter writer = new IntArrayBitWriter(memory, size * 32);
            int count = size;
            long start = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                coding.encodePositiveInt(writer, (i % bound) + 1);
            }
            writer.flush();
            long finish = System.currentTimeMillis();
            System.out.println(finish - start + " ms to write first " + count + " integers");
            IntArrayBitReader reader = new IntArrayBitReader(memory, writer.getSize());
            start = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                int v = coding.decodePositiveInt(reader);
                if (v != (i % bound) + 1) throw new RuntimeException("on read " + i);
            }
            finish = System.currentTimeMillis();
            System.out.println(finish - start + " ms to read first " + count + " integers");
        }
    }
