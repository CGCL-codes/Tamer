    public void perfTest3() {
        int size = SIZE;
        long start = System.currentTimeMillis();
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = (byte) i;
        }
        long end1 = System.currentTimeMillis();
        assertEquals(size, bytes.length);
        for (int i = 0; i < size; i++) {
            assertEquals((byte) i, bytes[i]);
        }
        long end2 = System.currentTimeMillis();
        System.out.println(String.format("byte[] write=%d  and read=%d", (end1 - start), (end2 - end1)));
    }
