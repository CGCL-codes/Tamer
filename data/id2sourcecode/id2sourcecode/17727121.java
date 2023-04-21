        @Override
        public void run() {
            if (socket == null) {
                while (!isInterrupted()) {
                    try {
                        socket = new DatagramSocket();
                        socket.setSoTimeout(60000);
                        log.info("Trying to connect to " + domintellConfig.getAddress() + " on port " + domintellConfig.getPort());
                        socket.connect(InetAddress.getByName(domintellConfig.getAddress()), domintellConfig.getPort());
                        log.info("Socket connected");
                        readerThread = new DomintellReaderThread(socket);
                        readerThread.start();
                        log.info("Reader thread started");
                        writerThread = new DomintellWriterThread(socket);
                        writerThread.start();
                        log.info("Writer thread started");
                        while (readerThread != null) {
                            readerThread.join(1000);
                            if (!readerThread.isAlive()) {
                                log.info("Reader thread is dead, clean and re-try to connect");
                                socket.disconnect();
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
