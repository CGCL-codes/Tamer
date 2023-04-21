    public void testLock(int readersCount, int writersCount, int readerWritersCount, boolean expectingDeadlock) {
        int finalvalue = writersCount + readerWritersCount;
        int totalThreads = writersCount + readerWritersCount + readersCount;
        Assert.assertEquals(0, generalPageLock.getWriters());
        Assert.assertEquals(0, generalPageLock.getReaders());
        ArrayList<Reader> readers = new ArrayList<Reader>();
        ArrayList<Writer> writers = new ArrayList<Writer>();
        ArrayList<ReaderWriter> readerWriters = new ArrayList<ReaderWriter>();
        for (int i = 0; i < readersCount; i++) {
            readers.add(new Reader(generalPageLock, i));
        }
        for (int i = 0; i < writersCount; i++) {
            writers.add(new Writer(generalPageLock, i));
        }
        for (int i = 0; i < readerWritersCount; i++) {
            readerWriters.add(new ReaderWriter(generalPageLock, i, expectingDeadlock));
        }
        Iterator<Reader> readersIterator = readers.iterator();
        Iterator<ReaderWriter> readerWritersIterator = readerWriters.iterator();
        Iterator<Writer> writersIterator = writers.iterator();
        int entered = 0;
        for (int i = 0; entered < totalThreads; i++) {
            if (i % 3 == 0) {
                if (readersIterator.hasNext()) {
                    new Thread(readersIterator.next()).start();
                    entered++;
                }
            } else if (i % 3 == 1) {
                if (writersIterator.hasNext()) {
                    new Thread(writersIterator.next()).start();
                    entered++;
                }
            } else {
                if (readerWritersIterator.hasNext()) {
                    new Thread(readerWritersIterator.next()).start();
                    entered++;
                }
            }
        }
        while (PageLockTest.finishedThreads < totalThreads) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Assert.fail("should not be interrupted");
            }
        }
        Assert.assertEquals("wrong final value!", finalvalue, PageLockTest.value + PageLockTest.failed);
        Assert.assertEquals(0, generalPageLock.getWriters());
        Assert.assertEquals(0, generalPageLock.getReaders());
    }
