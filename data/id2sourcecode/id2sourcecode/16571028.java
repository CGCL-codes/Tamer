    public static void testClassicPerformance(int size) {
        A.syso("Beginning performance test for size " + size + ".");
        long start = System.currentTimeMillis();
        int[] first = new int[size];
        for (int i = 0; i < first.length; ++i) {
            first[i] = (i + 2) * (i + 2);
        }
        while (first.length != 1) {
            int[] second = new int[first.length - 1];
            for (int i = 0; i < second.length; ++i) {
                second[i] = first[i + 1] - first[i];
            }
            first = second;
        }
        int result = first[0];
        long stop = System.currentTimeMillis();
        A.syso("Time: " + (stop - start) + " ms. Result: " + result + ".");
    }
