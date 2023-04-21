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
