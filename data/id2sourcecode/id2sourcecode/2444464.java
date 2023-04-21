    private void initThreads() {
        threadArray[0] = new Thread() {

            @Override
            public void run() {
                try {
                    collectionDao.createCollectionTables();
                    doScan();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        threadArray[1] = new Thread() {

            @Override
            public void run() {
                int count = 0;
                Collection entity = null;
                while (scannerThread.isAlive() || !entityQueue.isEmpty()) {
                    entity = readQueue();
                    if (entity == null) {
                        continue;
                    }
                    if (collectionDao.addCollection(entity)) {
                        count++;
                    }
                }
                doClearInvalidCollection();
                updatePostTable();
                log.info("Found " + count + " new item");
            }
        };
        scannerThread = threadArray[0];
        writerThread = threadArray[1];
        scannerThread.setName("Collection Scanner Thread");
        scannerThread.setDaemon(true);
        writerThread.setName("Collection Writer Thread");
        writerThread.setDaemon(true);
    }
