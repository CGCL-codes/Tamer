    static void testHashMap() {
        Random r = new Random(0);
        Object elem = new Object();
        long t1, t2;
        t1 = System.currentTimeMillis();
        HashMap<Integer, Object> arr = new HashMap<Integer, Object>();
        for (int i = 0; i < MAX_ROUNDS; i++) {
            int ref = r.nextInt(MAX_T) << S1;
            for (int j = 0; j < MAX_N; j++) {
                ref |= r.nextInt(MAX_N);
                arr.put(ref, elem);
                if (arr.get(ref) == null) throw new RuntimeException("element not set: " + i);
            }
        }
        t2 = System.currentTimeMillis();
        System.out.println("HashMap random write/read of " + arr.size() + " elements: " + (t2 - t1));
        int n = 0;
        t1 = System.currentTimeMillis();
        for (Object e : arr.values()) {
            n++;
        }
        t2 = System.currentTimeMillis();
        System.out.println("HashMap iteration over " + n + " elements: " + (t2 - t1));
    }
