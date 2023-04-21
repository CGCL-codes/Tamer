    public void run() {
        try {
            LOG.info("starting gateway on port " + _port + " pointing to port " + _destinationPort);
            _serverSocket = new ServerSocket(_port);
            while (true) {
                final Socket socket = _serverSocket.accept();
                LOG.info("new client is connected " + socket.getInetAddress());
                final InputStream incomingInputStream = socket.getInputStream();
                final OutputStream incomingOutputStream = socket.getOutputStream();
                final Socket outgoingSocket;
                try {
                    outgoingSocket = new Socket("localhost", _destinationPort);
                } catch (Exception e) {
                    LOG.warn("could not connect to " + _destinationPort);
                    continue;
                }
                final InputStream outgoingInputStream = outgoingSocket.getInputStream();
                final OutputStream outgoingOutputStream = outgoingSocket.getOutputStream();
                Thread writeThread = new Thread() {

                    public void run() {
                        try {
                            int read = -1;
                            while ((read = incomingInputStream.read()) != -1) {
                                outgoingOutputStream.write(read);
                            }
                        } catch (IOException e) {
                        }
                        LOG.info("write thread terminated");
                        synchronized (_runningThreads) {
                            _runningThreads.remove(this);
                        }
                    }

                    @Override
                    public void interrupt() {
                        try {
                            socket.close();
                            outgoingSocket.close();
                        } catch (IOException e) {
                            LOG.error("error on stopping closing sockets", e);
                        }
                        super.interrupt();
                    }
                };
                Thread readThread = new Thread() {

                    public void run() {
                        try {
                            int read = -1;
                            while ((read = outgoingInputStream.read()) != -1) {
                                incomingOutputStream.write(read);
                            }
                        } catch (IOException e) {
                        }
                        LOG.info("read thread terminated");
                        synchronized (_runningThreads) {
                            _runningThreads.remove(this);
                        }
                    }
                };
                writeThread.setDaemon(true);
                readThread.setDaemon(true);
                writeThread.start();
                readThread.start();
                _runningThreads.add(writeThread);
                _runningThreads.add(readThread);
            }
        } catch (SocketException e) {
            LOG.info("stopping gateway");
            synchronized (_runningThreads) {
                List<Thread> runningThreads = _runningThreads;
                for (Thread thread : runningThreads) {
                    thread.interrupt();
                }
            }
        } catch (Exception e) {
            LOG.error("error on gateway execution", e);
        }
    }
