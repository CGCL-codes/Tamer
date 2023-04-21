    private void recieve1() {
        new Thread() {

            @Override
            public void run() {
                Socket socket = null;
                BufferedInputStream bi = null;
                BufferedOutputStream bo = null;
                try {
                    try {
                        running = true;
                        feedBackStatus(TransferStatuses.Starting);
                        synchronized (lock) {
                            serverSocket = new ServerSocket(Settings.getFiletTansferPort());
                            socket = serverSocket.accept();
                            serverSocket.close();
                        }
                    } catch (Exception e) {
                        if (e instanceof java.net.BindException || e instanceof java.net.SocketException) {
                            if (serverSocket != null) {
                                serverSocket.close();
                                serverSocket = null;
                            }
                            Protocol.turnTransferAround(peerName, fileName);
                            Thread.sleep(500);
                            recieve2();
                            return;
                        } else {
                            e.printStackTrace();
                        }
                    }
                    bi = new BufferedInputStream(socket.getInputStream());
                    destinationFile = getDestFile();
                    if (!resuming) {
                        int n = 1;
                        while (!destinationFile.createNewFile()) {
                            destinationFile = checkfile(destinationFile, n);
                            n++;
                        }
                    } else {
                        if (!destinationFile.exists()) {
                            destinationFile.createNewFile();
                        }
                    }
                    bo = new BufferedOutputStream(new FileOutputStream(destinationFile, resuming));
                    feedBackStatus(TransferStatuses.Transferring);
                    starttime = System.currentTimeMillis();
                    processedBytes = 0;
                    int readbyte;
                    int tmp = 0;
                    while (running && (readbyte = bi.read()) != -1) {
                        bo.write(readbyte);
                        ++processedBytes;
                        ++tmp;
                        if (tmp > FLUSH_INTERVAL) {
                            bo.flush();
                            tmp = 0;
                        }
                    }
                    bo.flush();
                    if ((processedBytes + firstByteToSend - (resuming ? 1 : 0)) == totalsize) {
                        feedBackStatus(TransferStatuses.Finished);
                        feedBackProgress(100);
                    } else {
                        feedBackStatus(TransferStatuses.Failed);
                    }
                    running = false;
                    bi.close();
                    bo.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    feedBackStatus(TransferStatuses.Error);
                } finally {
                    try {
                        if (bi != null) {
                            bi.close();
                        }
                        if (bo != null) {
                            bo.close();
                        }
                        if (socket != null) {
                            socket.close();
                            socket = null;
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }.start();
    }
