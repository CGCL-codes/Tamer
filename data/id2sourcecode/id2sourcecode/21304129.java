    private BlockingQueue<Event> openLogWriter(final Writer writer, final String fileName) {
        try {
            if (outputFormat.equals(Format.HTML)) {
                writer.write("<HTML isdump=\"true\"><body>" + "<style>body {font-family:Helvetica; margin-left:15px;}</style>" + "<h2>Performance dump from GWT</h2>" + "<div>This file contains data that can be viewed with the " + "<a href=\"http://code.google.com/speedtracer\">SpeedTracer</a> " + "extension under the <a href=\"http://chrome.google.com/\">" + "Chrome</a> browser.</div><p><span id=\"info\">" + "(You must install the SpeedTracer extension to open this file)</span></p>" + "<div style=\"display: none\" id=\"traceData\" version=\"0.17\">\n");
            }
        } catch (IOException e) {
            System.err.println("Unable to write to gwt.speedtracerlog '" + (fileName == null ? "" : fileName) + "'");
            e.printStackTrace();
            return null;
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                try {
                    eventQueue.add(shutDownSentinel);
                    shutDownLatch.await();
                } catch (InterruptedException e) {
                }
            }
        });
        Thread logWriterWorker = new LogWriterThread(writer, fileName, eventQueue);
        logWriterWorker.setPriority((Thread.MIN_PRIORITY + Thread.NORM_PRIORITY) / 2);
        logWriterWorker.setDaemon(true);
        logWriterWorker.setName("SpeedTracerLogger writer");
        logWriterWorker.start();
        return eventQueue;
    }
