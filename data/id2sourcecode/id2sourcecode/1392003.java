        @Override
        public void run() {
            if (tc == null) {
                tc = new TelnetClient();
                tc.setConnectTimeout(10000);
                while (!isInterrupted()) {
                    try {
                        log.info("Trying to connect to " + lutronConfig.getAddress() + " on port " + lutronConfig.getPort());
                        tc.connect(lutronConfig.getAddress(), lutronConfig.getPort());
                        log.info("Telnet client connected");
                        readerThread = new LutronHomeWorksReaderThread(tc.getInputStream());
                        readerThread.start();
                        log.info("Reader thread started");
                        writerThread = new LutronHomeWorksWriterThread(tc.getOutputStream());
                        writerThread.start();
                        log.info("Writer thread started");
                        while (readerThread != null) {
                            readerThread.join(1000);
                            if (!readerThread.isAlive()) {
                                log.info("Reader thread is dead, clean and re-try to connect");
                                tc.disconnect();
                                readerThread = null;
                                writerThread.interrupt();
                                writerThread = null;
                            }
                        }
                    } catch (SocketException e) {
                        e.printStackTrace();
                        try {
                            Thread.sleep(15000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        try {
                            Thread.sleep(15000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
