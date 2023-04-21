    private static synchronized void prepareWriterThread() {
        if (writerThread == null) {
            writerThread = new WriterThread();
            writerThread.start();
            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    try {
                        writerThread.finishSendingMessages();
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            });
        }
    }
