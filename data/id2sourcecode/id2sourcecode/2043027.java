    @Test
    public void testMemoryMapped2() throws IOException {
        System.out.println("Writing/scanning " + TEMPORARY_SPACE);
        long start = System.nanoTime();
        final int length = 1000 * 1000 * 1000;
        HugeArrayBuilder<MutableTypes> hab = new HugeArrayBuilder<MutableTypes>() {

            {
                baseDirectory = TEMPORARY_SPACE;
                capacity = length;
                allocationSize = 32 * 1024 * 1024;
            }
        };
        final HugeArrayList<MutableTypes> list = hab.create();
        list.setSize(length);
        populate(list);
        System.out.println("... flushing");
        list.close();
        long mid = System.nanoTime();
        System.out.println("Sequential read test");
        HugeArrayBuilder<MutableTypes> hab2 = new HugeArrayBuilder<MutableTypes>() {

            {
                baseDirectory = TEMPORARY_SPACE;
                capacity = length;
                allocationSize = 32 * 1024 * 1024;
            }
        };
        final HugeArrayList<MutableTypes> list2 = hab2.create();
        list2.setSize(length);
        validate(list2);
        list2.close();
        long mid2 = System.nanoTime();
        HugeArrayBuilder<MutableTypes> hab3 = new HugeArrayBuilder<MutableTypes>() {

            {
                baseDirectory = TEMPORARY_SPACE;
                capacity = length;
                allocationSize = 32 * 1024 * 1024;
            }
        };
        System.out.println("Random access test");
        final HugeArrayList<MutableTypes> list3 = hab3.create();
        list3.setSize(length);
        for (int i = length - 1; i >= 0; i -= 101) {
            MutableTypes mb = list3.get(i);
            validate(mb, i);
            list3.recycle(mb);
        }
        list3.close();
        long end = System.nanoTime();
        System.out.printf("Took average of %,d ns to write, %,d ns to read and %,d ns to random read each element%n", (mid - start) / length, (mid2 - mid) / length, 101 * (end - mid2) / length);
    }
