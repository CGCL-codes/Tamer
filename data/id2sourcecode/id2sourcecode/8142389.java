    public void remoteExec(InputStream in, OutputStream out) throws JSchException, ConverterException, IOException {
        boolean isDisconnect = false;
        ChannelExec channel = null;
        OutputStream writeToProcess = null;
        InputStream readFromProcess = null;
        InputStream readErrorsFromProcess = null;
        try {
            if (session == null) {
                isDisconnect = true;
                connect();
            }
            channel = getChannel();
            long time = System.currentTimeMillis();
            if (logger.isDebugEnabled()) {
                logger.debug("remoteExec() - creating process and streams");
            }
            writeToProcess = channel.getOutputStream();
            readFromProcess = channel.getInputStream();
            readErrorsFromProcess = channel.getErrStream();
            ByteArrayOutputStream writeErrors = new ByteArrayOutputStream();
            if (logger.isDebugEnabled()) {
                logger.debug("remoteExec() - finished creating process and streams (time=" + (System.currentTimeMillis() - time) + "ms)");
            }
            if (logger.isDebugEnabled()) {
                logger.debug("remoteExec() - start writing/reading to/from process");
            }
            time = System.currentTimeMillis();
            Runnable writeToProcessThread = new CopyInputStream("Write to Process", in, writeToProcess);
            Thread readFromProcessThread = new Thread(new CopyInputStream("Read from Process", readFromProcess, out));
            Thread readErrorsThread = new Thread(new CopyInputStream("Read Errors", readErrorsFromProcess, writeErrors));
            readErrorsThread.start();
            readFromProcessThread.start();
            writeToProcessThread.run();
            if (logger.isDebugEnabled()) {
                logger.debug("remoteExec() - finished writing input (time=" + (System.currentTimeMillis() - time) + "ms)");
            }
            readFromProcessThread.join();
            if (logger.isDebugEnabled()) {
                logger.debug("remoteExec() - finished reading output (time=" + (System.currentTimeMillis() - time) + "ms)");
            }
            readErrorsThread.join();
            if (logger.isDebugEnabled()) {
                logger.debug("remoteExec() - finished reading errors (time=" + (System.currentTimeMillis() - time) + "ms)");
            }
            if (logger.isDebugEnabled()) {
                logger.debug("remoteExec() - process has terminated (time=" + (System.currentTimeMillis() - time) + "ms)");
            }
            String error = new String(writeErrors.toByteArray(), "UTF-8");
            if (error != null && error.length() > 0) {
                throw new IOException(error);
            }
            logger.debug("remoteExec() -  exit code: " + channel.getExitStatus());
        } catch (InterruptedException e) {
            logger.error(e);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (isDisconnect) {
                if (session != null) {
                    session.disconnect();
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("remoteExec() - end");
        }
    }
