    private void dumpTag() {
        final BlockingQueue<List<Tag>> queue = new ArrayBlockingQueue<List<Tag>>(capacity);
        final String consoleId = "dumpTag";
        final Thread[] threads = new Thread[2];
        threads[0] = new Thread("tagDumpReaderThread") {

            @Override
            public void run() {
                Thread writerThread = threads[1];
                TagDao tagDao = new TagDao(BaseDao.ConnType.DUMP);
                int next = 1, progress = 0, tmp = 0;
                Paging paging = new Paging(pageSize);
                List<Tag> list = null;
                boolean offer = false;
                do {
                    paging.setPageNumber(next);
                    try {
                        list = tagDao.listTags(paging);
                    } catch (Throwable t) {
                        t.printStackTrace();
                        return;
                    }
                    tmp = Math.round(paging.getPageNumber() * 1f / paging.getPageTotal() * 100);
                    if (progress != tmp) {
                        progress = tmp;
                        console.flushablePrint(consoleId, "Tags progress: " + progress + "%");
                    }
                    offer = false;
                    do {
                        try {
                            offer = queue.offer(list, 5, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                        }
                        if (!writerThread.isAlive()) {
                            return;
                        }
                    } while (!offer);
                    if (paging.hasNext()) {
                        next++;
                    }
                } while (paging.hasNext());
            }
        };
        threads[1] = new Thread("tagDumpWriterThread") {

            @Override
            public void run() {
                Thread readerThread = threads[0];
                TagDao tagDao = new TagDao();
                List<Tag> list = null;
                while (!queue.isEmpty() || readerThread.isAlive()) {
                    list = null;
                    do {
                        try {
                            list = queue.poll(5, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                        }
                        if (list == null && !readerThread.isAlive()) {
                            break;
                        }
                    } while (list == null);
                    try {
                        tagDao.insertTags(list);
                    } catch (Throwable t) {
                        t.printStackTrace();
                        countDownLatch.countDown();
                        return;
                    }
                }
                countDownLatch.countDown();
                console.remove(consoleId);
                console.println("Tags table dump finish!\t\t\t\t\t\t");
            }
        };
        console.println("Tags table dump start.");
        for (Thread thread : threads) {
            thread.start();
        }
    }
