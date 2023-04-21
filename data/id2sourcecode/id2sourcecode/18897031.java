    void sendFile() throws IOException, InterruptedException {
        INetwork jn = (INetwork) Main.getConfig().getObjectProperty("net");
        currentUploadApprovedCommand = new UploadApprovedCommand(remoteuser, filename);
        jn.sendCommand(currentUploadApprovedCommand);
        if (!currentUploadApprovedCommand.sync(120000)) {
            msg = "Answer to UPLOAD timed out.";
            status = FAILED;
            return;
        }
        if (currentException != null) {
            throw currentException;
        }
        Debug.log(Debug.UPLOAD, "Starting upload.");
        jn.sendCommand(new UploadNotifyStartCommand());
        status = IN_TRANSIT;
        try {
            s.setSoTimeout(1000);
            bos = new BufferedOutputStream(s.getOutputStream());
            fis = new RandomAccessFile(f, "r");
            if (curBytes > 0) {
                fis.skipBytes((int) curBytes);
            }
            long bytes = totalBytes - curBytes;
            String b = String.valueOf(bytes);
            bos.write(b.getBytes());
            bos.flush();
            startTime = System.currentTimeMillis();
            while (curBytes < totalBytes) {
                if (die) return;
                int amount = 0;
                try {
                    amount = fis.read(readBuf);
                } catch (InterruptedIOException iie) {
                    continue;
                }
                curBytes += amount;
                bos.write(readBuf, 0, amount);
            }
            status = COMPLETE;
            msg = "Upload complete.";
        } finally {
            jn.sendCommand(new UploadNotifyFinishCommand());
        }
    }
