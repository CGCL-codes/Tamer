        public void executeTask() {
            long startTime = System.currentTimeMillis();
            TestRW testObj = (TestRW) testMutex.getWriteLock();
            totalBlockTime += System.currentTimeMillis() - startTime;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            int writeVal = testObj.readVal() + 1;
            testObj.writeVal(writeVal);
            testMutex.releaseWriteLock();
        }
