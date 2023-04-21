    @Test
    public void testBehaviour() throws Exception {
        final int max = (int) 10E+2;
        Runnable writeThread = new Runnable() {

            public void run() {
                synchronized (lock) {
                    try {
                        for (int i = 0; i < max; i++) {
                            TSAdapterEntry out = new TSAdapterEntry();
                            out.annotations = new HashSet<TripleEntry>();
                            out.clientInfo = null;
                            out.data = new HashSet<TripleEntry>();
                            out.data.add(getTripleEntry(i));
                            out.operationID = (long) i;
                            out.space = new SpaceURI("urn:test:space:root");
                            out.tripleset = new HashSet<java.net.URI>();
                            out.writeOutSMEntry = true;
                            adapter.getSpace().write(out, null, Long.MAX_VALUE);
                            if (IS_RANDOM) Thread.sleep(random.nextInt(100));
                            if (out.writeOutSMEntry == true) {
                                adapter.getSpace().take(new OutSMEntry(), null, Long.MAX_VALUE);
                                if (IS_RANDOM) Thread.sleep(random.nextInt(100));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Runnable readThread = new Runnable() {

            public void run() {
                try {
                    for (int i = 0; i < max; i++) {
                        RdTSAdapterEntry rd = new RdTSAdapterEntry();
                        rd.clientInfo = null;
                        rd.operationID = (long) i;
                        rd.space = new SpaceURI("tsc://example.org:1234/rootspace/subspace/deepersubspace/thisspace");
                        rd.permittedSpaces = new SpaceSelection();
                        rd.kind = ReadType.IN;
                        rd.query = String.format("CONSTRUCT { <%s> <%s> ?o } WHERE { <%s> <%s> ?o }", "http://example.com/book/book" + i, "http://purl.org/dc/elements/1.1/title", "http://example.com/book/book" + i, "http://purl.org/dc/elements/1.1/title");
                        adapter.getSpace().write(rd, null, Long.MAX_VALUE);
                        if (IS_RANDOM) Thread.sleep(random.nextInt(100));
                        RdMetaMEntry result = (RdMetaMEntry) adapter.getSpace().take(new RdMetaMEntry(), null, Long.MAX_VALUE);
                        Assert.assertEquals(result.data.size(), 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t = new Thread(writeThread);
        t.start();
        Thread t2 = new Thread(readThread);
        t2.start();
        Thread.sleep(1000);
        synchronized (lock) {
            System.out.println("OK!");
        }
    }
