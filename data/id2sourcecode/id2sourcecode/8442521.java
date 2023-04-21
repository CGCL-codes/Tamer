    public static void main(String[] args) throws Exception {
        final int readers = Integer.parseInt(args[0]);
        final int upgraders = Integer.parseInt(args[1]);
        final int writers = Integer.parseInt(args[2]);
        final int runTimeSeconds = Integer.parseInt(args[3]);
        final UpgradableLock lock = new UpgradableLock();
        List<TestThread> threads = new ArrayList<TestThread>();
        for (int i = 0; i < readers; i++) {
            TestThread t = new TestThread("reader " + i) {

                public void run() {
                    while (!cStop) {
                        lock.lockForRead(this);
                        try {
                            long value = cSharedValue;
                            String.valueOf(value);
                            long again = cSharedValue;
                            if (again != value) {
                                throw new AssertionError("" + again + " != " + value);
                            }
                            mReadCount++;
                        } finally {
                            lock.unlockFromRead(this);
                        }
                    }
                }
            };
            threads.add(t);
            t.start();
        }
        for (int i = 0; i < upgraders; i++) {
            TestThread t = new TestThread("upgrader " + i) {

                public void run() {
                    while (!cStop) {
                        lock.lockForUpgrade(this);
                        try {
                            long value = cSharedValue;
                            String.valueOf(value);
                            assert (cSharedValue == value);
                            lock.lockForWrite(this);
                            assert (cSharedValue == value);
                            try {
                                cSharedValue = value + 1;
                            } finally {
                                lock.unlockFromWrite(this);
                            }
                            mAdjustCount++;
                        } finally {
                            lock.unlockFromUpgrade(this);
                        }
                    }
                }
            };
            threads.add(t);
            t.start();
        }
        for (int i = 0; i < writers; i++) {
            TestThread t = new TestThread("writer " + i) {

                public void run() {
                    while (!cStop) {
                        lock.lockForWrite(this);
                        try {
                            long value = cSharedValue;
                            String.valueOf(value);
                            assert (cSharedValue == value);
                            cSharedValue = value + 1;
                            mAdjustCount++;
                        } finally {
                            lock.unlockFromWrite(this);
                        }
                    }
                }
            };
            threads.add(t);
            t.start();
        }
        Thread.sleep(1000L * runTimeSeconds);
        cStop = true;
        for (TestThread t : threads) {
            t.join();
        }
        long reads = 0;
        for (TestThread t : threads) {
            reads += t.mReadCount;
        }
        long expected = 0;
        for (TestThread t : threads) {
            expected += t.mAdjustCount;
        }
        System.out.println("Reads:    " + reads);
        System.out.println("Expected: " + expected);
        System.out.println("Actual:   " + cSharedValue);
        if (expected == cSharedValue) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILURE");
        }
        System.out.println();
        for (TestThread t : threads) {
            System.out.println(t.mReadCount + ", " + t.mAdjustCount);
        }
    }
