    public static void performTest(THREAD_WORK_TYPE readType) throws Exception {
        AppendOnlyPrimitiveLongList list = new AppendOnlyPrimitiveLongList();
        AppendOnlyListTest readerThread = new AppendOnlyListTest(list, readType, new File("tmp/read.txt"));
        AppendOnlyListTest writerThread = new AppendOnlyListTest(list, THREAD_WORK_TYPE.write, new File("tmp/write.txt"));
        readerThread.start();
        writerThread.start();
        readerThread.join();
        writerThread.join();
        long[] readResults = readerThread.getResults();
        long[] writeResults = writerThread.getResults();
        for (int i = 0; i < writeResults.length; i++) {
            long diff = readResults[i] - writeResults[i];
            Assert.assertEquals(diff, 0);
        }
        readerThread.discard();
        writerThread.discard();
    }
