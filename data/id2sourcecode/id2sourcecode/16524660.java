    protected void processRequest(final InputStream in, final OutputStream out) throws IOException {
        ChannelExec channel = null;
        OutputStream channelOut = null;
        InputStream channelIn = null;
        InputStream channelErrorIn = null;
        int numBytes = 0;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Executing command: [" + getCommand() + "].");
        }
        try {
            channel = getChannel();
            channelOut = channel.getOutputStream();
            channelIn = channel.getInputStream();
            channelErrorIn = channel.getErrStream();
            final ByteArrayOutputStream errorOut = new ByteArrayOutputStream();
            final CopyInputStream writeToChannel = new CopyInputStream("Write to Process", in, channelOut);
            final Thread writeToProcessThread = new Thread(writeToChannel);
            final Thread readErrorsThread = new Thread(new CopyInputStream("Read Errors", channelErrorIn, errorOut));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Start seperate io threads.");
            }
            readErrorsThread.start();
            writeToProcessThread.start();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Start looping around read process.");
            }
            int i;
            final byte[] tmp = new byte[1024];
            while (true) {
                i = 0;
                while (channelIn.available() > 0) {
                    i = channelIn.read(tmp);
                    if (i < 0) {
                        break;
                    }
                    out.write(tmp, 0, i);
                    numBytes += i;
                }
                if (channel.isClosed()) {
                    break;
                }
            }
            readErrorsThread.join();
            out.flush();
            LOGGER.info("Finished reading incoming. Checking error conditions.");
            final String error = new String(errorOut.toByteArray(), "UTF-8");
            if (error != null && error.length() > 0) {
                throw new IOException(error);
            }
            if (writeToChannel.getException() != null) {
                throw new IOException(writeToChannel.getException());
            }
            if (numBytes < 1 && channel.getExitStatus() == 0) {
                throw new IOException("Disappointing response from command [" + getCommand() + "]. More then [" + numBytes + "] bytes or RC != 0 expected.");
            }
        } catch (final InterruptedException e) {
            LOGGER.error(e, e);
        } catch (final JSchException e) {
            throw new IOException(e);
        } finally {
            LOGGER.info("Number of bytes received from channel: [" + numBytes + "].");
            if (channel != null) {
                LOGGER.info("Exit code: [" + channel.getExitStatus() + "].");
                channel.disconnect();
            }
            if (out != null) {
                out.close();
            }
            if (channelIn != null) {
                channelIn.close();
            }
        }
    }
