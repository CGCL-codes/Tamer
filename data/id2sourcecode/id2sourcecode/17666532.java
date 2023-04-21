    public void test2() {
        int size = SIZE;
        Bytes bytes = BytesFactory.getBytes();
        long start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            bytes.set(i, (byte) i);
        }
        long end1 = System.currentTimeMillis();
        assertEquals(size, bytes.getRealSize());
        assertEquals(size / Layer3Converter.DEFAULT_BYTES_SIZE + 1, bytes.getNbBlocks());
        for (int i = 0; i < size; i++) {
            assertEquals((byte) i, bytes.get(i));
        }
        long end2 = System.currentTimeMillis();
        System.out.println(String.format("Bytes  write=%d  and read=%d", (end1 - start), (end2 - end1)));
    }
