    public void remoteExec(Reader in, Writer out, Charset processInCharset, Charset processOutCharset, CharacterNormalizer procInNormalizer, CharacterNormalizer procOutNormalizer) throws JSchException, ConverterException, IOException {
        boolean isDisconnect = false;
        ChannelExec channel = null;
        Writer writeToProcess = null;
        Reader readFromProcess = null;
        Reader readErrorsFromProcess = null;
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
            writeToProcess = new BufferedWriter(new OutputStreamWriter((channel.getOutputStream()), processInCharset));
            readFromProcess = new BufferedReader(new InputStreamReader((channel.getInputStream()), processOutCharset));
            readErrorsFromProcess = new BufferedReader(new InputStreamReader((channel.getErrStream())));
            StringWriter writeErrors = new StringWriter();
            if (logger.isDebugEnabled()) {
                logger.debug("remoteExec() - finished creating process and streams (time=" + (System.currentTimeMillis() - time) + "ms)");
            }
            if (logger.isDebugEnabled()) {
                logger.debug("remoteExec() - start writing/reading to/from process");
            }
            time = System.currentTimeMillis();
            Runnable writeToProcessThread = new CopyCharStream("Write to Process", in, writeToProcess, procInNormalizer);
            Thread readFromProcessThread = new Thread(new CopyCharStream("Read from Process", readFromProcess, out, procOutNormalizer));
            Thread readErrorsThread = new Thread(new CopyCharStream("Read Errors", readErrorsFromProcess, writeErrors, null));
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
            String error = writeErrors.getBuffer().toString();
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
