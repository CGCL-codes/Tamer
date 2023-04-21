    protected void compileQueries() throws IOException {
        final DataServiceDispatcher d = DataServiceDispatcher.getInstance();
        final ThreadGroup queryCompile = new ThreadGroup("Query compilation") {

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                final QueryCompileWorker w = (QueryCompileWorker) t;
                w.setErrorStatus();
            }
        };
        System.out.println("-- Compiling swatch queries");
        final List<QueryCompileWorker> threads = new ArrayList<QueryCompileWorker>(swatchQueryFilenames.length);
        for (String[] pair : swatchQueryFilenames) {
            final String channelname = pair[0];
            final String filename = pair[1];
            final String fullpath = DataServiceDispatcher.DAYTONASERVICEPATH + filename;
            final InputChannelInterface c = getChannelFromListByName(d.getInputChannels(), channelname);
            DaytonaSwatchQuery dsq = null;
            for (final DaytonaSwatchQuery q : daytonaSwatchQueries.values()) {
                if (q.getQueryFilePath().equals(fullpath)) dsq = q;
            }
            if (dsq == null) {
                dsq = new DaytonaSwatchQuery(filename, fullpath, c);
                final QueryCompileWorker w = new QueryCompileWorker(queryCompile, filename, dsq);
                threads.add(w);
                w.start();
            } else {
                dsq.addChannel(c);
            }
            daytonaSwatchQueries.put(c, dsq);
        }
        boolean swatchError = false;
        while (true) {
            boolean working = false;
            for (final QueryCompileWorker t : threads) {
                if (t.isErrorStatus()) {
                    swatchError = true;
                }
                if (t.isAlive()) working = true;
            }
            if (!working) break;
            try {
                Thread.sleep(SPINSLEEP);
            } catch (InterruptedException e) {
            }
        }
        if (swatchError) throw new IOException("query compilation failed");
        System.out.println("-- Finished compiling swatch queries");
        System.out.println("-- Compiling detail queries");
        threads.clear();
        for (String[] pair : detailQueryFilenames) {
            final String channelname = pair[0];
            final String filename = pair[1];
            final String fullpath = DataServiceDispatcher.DAYTONASERVICEPATH + filename;
            final InputChannelInterface c = getChannelFromListByName(d.getInputChannels(), channelname);
            DaytonaDetailQuery dsq = null;
            for (final DaytonaDetailQuery q : daytonaDetailQueries.values()) {
                if (q.getQueryFilePath().equals(fullpath)) dsq = q;
            }
            if (dsq == null) {
                dsq = new DaytonaDetailQuery(filename, fullpath, c);
                final QueryCompileWorker w = new QueryCompileWorker(queryCompile, filename, dsq);
                threads.add(w);
                w.start();
            } else {
                dsq.addChannel(c);
            }
            daytonaDetailQueries.put(c, dsq);
        }
        boolean detailError = false;
        while (true) {
            boolean working = false;
            for (final QueryCompileWorker t : threads) {
                if (t.isErrorStatus()) detailError = true;
                if (t.isAlive()) working = true;
            }
            if (!working) break;
            try {
                Thread.sleep(SPINSLEEP);
            } catch (InterruptedException e) {
            }
        }
        if (detailError) throw new IOException("query compilation failed");
        System.out.println("-- Finished compiling detail queries");
    }
