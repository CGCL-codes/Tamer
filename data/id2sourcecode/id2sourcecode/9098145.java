    public void run() {
        log.debug("Opening streams");
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        this.lengthInBytes = source.length();
        StringBuilder logoutput = new StringBuilder();
        try {
            try {
                fileInputStream = new FileInputStream(source);
            } catch (FileNotFoundException e) {
                log.error("Opening input Stream Failed");
                throw e;
            }
            try {
                fileOutputStream = new FileOutputStream(destination);
            } catch (FileNotFoundException e1) {
                log.error("Opening output Stream Failed");
                throw e1;
            }
            log.debug("Opening channels");
            inputChannel = fileInputStream.getChannel();
            outputChannel = fileOutputStream.getChannel();
            long overallBytesTransfered = 0L;
            try {
                long t1 = System.currentTimeMillis();
                while (stop == false) {
                    long bytesToTransfer = Math.min(chunckSizeInBytes, lengthInBytes - overallBytesTransfered);
                    long bytesTransfered = 0;
                    log.debug("Transfer bytes: " + bytesToTransfer);
                    try {
                        bytesTransfered = inputChannel.transferTo(overallBytesTransfered, bytesToTransfer, outputChannel);
                    } catch (IOException e) {
                        log.error("Opening output Stream Failed");
                        throw e;
                    }
                    overallBytesTransfered += bytesTransfered;
                    if (overallBytesTransfered == lengthInBytes) {
                        stop = true;
                        long t2 = System.currentTimeMillis();
                        logoutput.append("Copied ").append(source).append(" -> ").append(destination).append(" ");
                        if (lengthInBytes > MB) {
                            logoutput.append(lengthInBytes / MB).append("MiB");
                        } else {
                            logoutput.append(lengthInBytes / KB).append("KiB");
                        }
                        logoutput.append(" in ").append((t2 - t1) / 1000f).append(" seconds").append(" [").append((lengthInBytes / 1024f / 1024f) / ((t2 - t1) / 1000f)).append("MiB/s]");
                        log.info(logoutput.toString());
                    }
                }
            } catch (IOException e) {
                log.error("Copy failed");
                throw e;
            } finally {
                fileInputStream.close();
                fileOutputStream.close();
                inputChannel.close();
                outputChannel.close();
            }
        } catch (IOException e) {
            log.error("Stacktrace:", e);
        } finally {
            interrupt();
        }
    }
