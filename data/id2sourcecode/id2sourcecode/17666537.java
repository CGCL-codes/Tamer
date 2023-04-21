    public void test22() {
        int size = SIZE;
        Bytes2 bytes = new Bytes2(100);
        long start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            bytes.set(i, (byte) i);
        }
        long end1 = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            assertEquals((byte) i, bytes.get(i));
        }
        long end2 = System.currentTimeMillis();
        System.out.println(String.format("Bytes2 write=%d  and read=%d", (end1 - start), (end2 - end1)));
    }
