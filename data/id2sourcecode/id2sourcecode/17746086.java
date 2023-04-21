    private static void loops() {
        for (int i = 0; i < readThreadCount; i++) {
            readExec.execute(new Runnable() {

                @Override
                public void run() {
                    final long start = System.currentTimeMillis();
                    Random r = new Random();
                    int c = 0;
                    for (int i = 0; i < readCount; ++i) {
                        int n = r.nextInt();
                        cache.get(String.valueOf(n));
                        ++c;
                    }
                    totalReadCount.addAndGet(c);
                    long escaped = System.currentTimeMillis() - start;
                    totalReadTime.addAndGet(escaped);
                    timePerReadThread.add(escaped);
                }
            });
        }
        for (int i = 0; i < writeThreadCount; i++) {
            writeExec.execute(new Runnable() {

                @Override
                public void run() {
                    final long start = System.currentTimeMillis();
                    Random r = new Random();
                    int c = 0;
                    for (int i = 0; i < writeCount; ++i) {
                        int n = r.nextInt();
                        String s = String.valueOf(n);
                        cache.put(s, s);
                        ++c;
                    }
                    totalWriteCount.addAndGet(c);
                    long escaped = System.currentTimeMillis() - start;
                    totalWriteTime.addAndGet(escaped);
                    timePerWriteThread.add(escaped);
                }
            });
        }
    }
