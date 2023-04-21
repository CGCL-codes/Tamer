    public synchronized void run() {
        long tempWritten = 0;
        active = true;
        byte[] readTransport = new byte[transportSize];
        try {
            InputStream in = null;
            while (active) {
                if (numOutputStreams == 0) {
                    if (in != null) {
                        in.close();
                        in = null;
                        logInfo("closed in");
                    }
                    while (active && numOutputStreams == 0) {
                        logDebug("waiting forever");
                        doWait(0);
                    }
                    if (active) {
                        in = inFactory.create();
                        logInfo("opened in");
                    }
                }
                if (active) {
                    int read = in.read(readTransport);
                    writeToBuffers(readTransport, read);
                    if (LOGGER.isDebugEnabled()) {
                        tempWritten += read;
                        if (tempWritten > tempMarkMb) {
                            logDebug("written " + tempWritten + " bytes to " + numOutputStreams + " streams");
                            tempWritten = 0;
                            if (tempMarkMb < 256 * 1024 * 1024) {
                                tempMarkMb *= 2;
                            }
                        }
                    }
                    doWait(5);
                }
            }
            logInfo("became inactive");
            for (int i = 0; i < numOutputStreams; i++) {
                out[i].close();
            }
            if (in != null) {
                in.close();
                in = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logInfo("exited");
    }
