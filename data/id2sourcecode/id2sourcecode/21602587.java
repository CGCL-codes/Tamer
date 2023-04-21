        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            int readPercentage = 100 - writePercentage;
            Random r = new Random();
            int randomAction;
            int randomKeyInt;
            try {
                startPoint.await();
                log.info("Starting thread: " + getName());
            } catch (InterruptedException e) {
                log.warn(e);
            }
            int i = 0;
            while (requestsLeft.getAndDecrement() > -1) {
                randomAction = r.nextInt(100);
                randomKeyInt = r.nextInt(numberOfKeys - 1);
                String key = getKey(randomKeyInt);
                Object result = null;
                if (randomAction < readPercentage) {
                    long start = System.currentTimeMillis();
                    try {
                        result = cacheWrapper.get(bucketId, key);
                    } catch (Exception e) {
                        log.warn(e);
                        nrFailures++;
                    }
                    readDuration += System.currentTimeMillis() - start;
                    reads++;
                } else {
                    String payload = generateRandomString(sizeOfValue);
                    long start = System.currentTimeMillis();
                    try {
                        cacheWrapper.put(bucketId, key, payload);
                        logProgress(i, null);
                    } catch (Exception e) {
                        log.warn(e);
                        nrFailures++;
                    }
                    writeDuration += System.currentTimeMillis() - start;
                    writes++;
                }
                i++;
                logProgress(i, result);
            }
        }
