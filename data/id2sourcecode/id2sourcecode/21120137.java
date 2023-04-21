    public void testThreadLocalLogFiles() throws Exception {
        Layout layout = new SimpleLayout();
        Thread[] threads = new Thread[3];
        LogWriter[] writers = new LogWriter[threads.length];
        for (int i = 0; i < threads.length; i++) {
            String filename = "threadlog-" + i + ".log";
            File f = new File(filename);
            if (f.exists()) f.delete();
            Appender a = LogUtil.createAsyncFileAppender(layout, filename, false);
            writers[i] = new LogWriter(i, a, ITERATIONS, filename);
            threads[i] = new Thread(writers[i]);
        }
        Appender localAppender = new ThreadLocalAppender();
        testlog.addAppender(localAppender);
        testlog.info("Staring threads...");
        for (int i = 0; i < threads.length; i++) threads[i].start();
        testlog.info("Joining threads...");
        for (int i = 0; i < threads.length; i++) threads[i].join();
        testlog.removeAppender(localAppender);
        testlog.info("Reading log files...");
        for (int i = 0; i < writers.length; i++) {
            LogWriter writer = writers[i];
            String filename = writer.getFilename();
            LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(filename)));
            String line;
            while ((line = reader.readLine()) != null) {
                assertTrue("'id=" + writer.getId() + "' not found! line='" + line + "'", line.indexOf("id=" + writer.getId()) >= 0);
            }
            int lastLine = reader.getLineNumber();
            reader.close();
            assertEquals(ITERATIONS, lastLine);
        }
    }
