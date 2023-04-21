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
