    public void testQueue() {
        FastPriorityQueueLong queue = new FastPriorityQueueLong(MAX_DISTANCE, TOP);
        List<OBResultInvertedInt<Long>> result = new ArrayList<OBResultInvertedInt<Long>>(TOTAL);
        int[] counts = new int[MAX_DISTANCE + 1];
        Random r = new Random();
        int i = 0;
        while (i < TOTAL) {
            int distance = r.nextInt(MAX_DISTANCE + 1);
            queue.add(distance, distance);
            result.add(new OBResultInvertedInt(distance, distance, distance));
            i++;
        }
        Collections.sort(result);
        Iterator<OBResultInvertedInt<Long>> it = result.iterator();
        i = 0;
        long[] data = queue.get();
        for (long l : data) {
            if (i >= TOP) {
                break;
            }
            OBResultInvertedInt<Long> e = it.next();
            assertTrue("Found: " + e.getDistance() + " but fast got: " + l, e.getDistance() == l);
            i++;
        }
    }
