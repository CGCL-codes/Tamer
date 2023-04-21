    @Test
    public void doConcurrentWriteAndIn() throws Exception {
        this.rootSpace = this.tsclient.create(this.rootSpace);
        Thread writeThread = new Thread() {

            public void run() {
                try {
                    for (Statement t : statements) {
                        tsclient.out(t, rootSpace);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        writeThread.start();
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread() {

                public void run() {
                    try {
                        Set<Statement> result = tsclient.in(new Template("SELECT * WHERE { ?s ?p ?o . }"), rootSpace, 20000);
                        assertNotNull(result);
                        assertEquals(1, result.size());
                        incrementCount();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            threads[i].start();
            Thread.sleep(100);
        }
        for (Thread t : threads) {
            t.join();
        }
        assertEquals(THREAD_COUNT, this.getCount());
        Set<Set<Statement>> result = this.tsclient.rdmultiple(new Template("SELECT * WHERE { ?s ?p ?o . }"), this.rootSpace, 10000);
        assertNotNull(result);
        assertEquals(1, result.size());
        Set<Statement> st = result.iterator().next();
        this.tsclient.destroy(this.rootSpace);
    }
