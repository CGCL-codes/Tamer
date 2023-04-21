    private void dumpPost() {
        final BlockingQueue<List<Post>> queue = new ArrayBlockingQueue<List<Post>>(capacity);
        final String consoleId = "dumpPost";
        final Thread[] threads = new Thread[2];
        threads[0] = new Thread("postDumpReaderThread") {

            @Override
            public void run() {
                Thread writerThread = threads[1];
                PostDao postDao = new PostDao(BaseDao.ConnType.DUMP);
                int next = 1, progress = 0, tmp = 0;
                Paging paging = new Paging(pageSize);
                Map<String, String[]> query = new HashMap<String, String[]>();
                query.put("order", new String[] { "desc id" });
                List<Post> list = null;
                do {
                    paging.setPageNumber(next);
                    try {
                        list = postDao.listPosts(paging);
                    } catch (Throwable t) {
                        t.printStackTrace();
                        return;
                    }
                    tmp = Math.round(paging.getPageNumber() * 1f / paging.getPageTotal() * 100);
                    if (progress != tmp) {
                        progress = tmp;
                        console.flushablePrint(consoleId, "Post progress: " + progress + "%");
                    }
                    boolean offer = false;
                    do {
                        offer = false;
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
        threads[1] = new Thread("postDumpWriterThread") {

            @Override
            public void run() {
                Thread readerThread = threads[0];
                PostDao postDao = new PostDao(BaseDao.ConnType.DEFAULT);
                List<Post> list = null;
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
                        postDao.insertPosts(list);
                    } catch (Throwable t) {
                        t.printStackTrace();
                        countDownLatch.countDown();
                        return;
                    }
                }
                countDownLatch.countDown();
                console.remove(consoleId);
                console.println("Post table dump finish!");
            }
        };
        console.println("Post table dump start.");
        for (Thread thread : threads) {
            thread.start();
        }
    }
