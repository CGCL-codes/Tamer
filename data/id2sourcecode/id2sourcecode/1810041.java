    public void addToOutbox(Object obj) {
        if (writeThread == null) {
            writeThread = new QueueProcessorThread("SocketClient-WriteThread") {

                public void process(Object object) {
                    if (conn == null) {
                        int wait = initialWaitValue;
                        while (out == null || in == null) {
                            if (!isRunning()) return;
                            try {
                                String serv = getNextServer();
                                retryCount++;
                                Logger.info("[SocketClient] Trying to connect to: " + serv + " to send: " + object);
                                if (disconnected) throw new IOException();
                                conn = openConnection(serv);
                                out = conn.openOutputStream();
                                in = conn.openInputStream();
                                updateState(CONNECTING);
                                retryCount = 0;
                                wait = initialWaitValue;
                            } catch (SecurityException s) {
                                close(conn, in, out);
                                updateState(DISCONNECTED);
                                Logger.info(s);
                                securityException();
                                return;
                            } catch (Exception x) {
                                close(conn, in, out);
                                if (x instanceof IOException) {
                                    Logger.info(x.toString());
                                } else {
                                    Logger.info(x);
                                }
                                if (pauseReconnectOnFailure && retryCount > maxRetries) {
                                    updateState(DISCONNECTED_AND_PAUSED);
                                    synchronized (this) {
                                        try {
                                            wait();
                                        } catch (Exception e) {
                                            Logger.info(e);
                                        }
                                    }
                                    updateState(DISCONNECTED);
                                    retryCount = 0;
                                } else {
                                    updateState(DISCONNECTED);
                                    try {
                                        Thread.sleep(wait);
                                        wait = wait * retryWaitMultiplier;
                                        if (wait > maxWaitTimeMillis) {
                                            wait = maxWaitTimeMillis;
                                        }
                                    } catch (InterruptedException ex) {
                                        Logger.info(ex);
                                    }
                                }
                            }
                        }
                        connected(in, out);
                        readThread = new Thread(SocketClient.this, "SocketClient-ReadThread");
                        readThread.start();
                    }
                    try {
                        Logger.info("[SocketClient] sending object: " + object);
                        updateState(COMMUNICATING);
                        if (disconnected) throw new IOException();
                        write(out, object);
                        out.flush();
                        updateState(CONNECTED);
                    } catch (Exception ex) {
                        Logger.info("[SocketClient] Exception during a write to socket");
                        Logger.info(ex);
                        addToOfflineBox(object, true);
                        shutdownConnection();
                    }
                }
            };
            updateState(CONNECTING);
            writeThread.start();
        }
        writeThread.addToInbox(obj);
    }
