    private void testConcurrent() throws Exception {
        Runnable insert = new Runnable() {

            public void run() {
                insert();
            }
        };
        Runnable read = new Runnable() {

            public void run() {
                read();
            }
        };
        Runnable remove = new Runnable() {

            public void run() {
                remove();
            }
        };
        Thread t[] = new Thread[readers + writers + removers];
        int c = 0;
        for (int i = 0; i < writers; i++) {
            t[c++] = new Thread(insert);
        }
        for (int i = 0; i < readers; i++) {
            t[c++] = new Thread(read);
        }
        for (int i = 0; i < removers; i++) {
            t[c++] = new Thread(remove);
        }
        for (int i = 0; i < t.length; i++) {
            t[i].start();
        }
        System.err.println("wait for complete");
        for (int i = 0; i < t.length; i++) {
            t[i].join();
        }
    }
