    public byte[] remoteExec(byte[] input) throws IOException {
        boolean isDisconnect = false;
        byte[] ret = null;
        ChannelExec channel = null;
        try {
            if (session == null) {
                isDisconnect = true;
                connect();
            }
            channel = getChannel();
            if (input != null) {
                OutputStream processOut = channel.getOutputStream();
                processOut.write(input);
                processOut.close();
            }
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            InputStream processOut = channel.getInputStream();
            int i;
            while ((i = processOut.read()) != -1) {
                buffer.write(i);
            }
            logger.debug("remoteExec() -  exit code: " + channel.getExitStatus());
            ret = buffer.toByteArray();
        } catch (JSchException e) {
            throw new IOException(e.getMessage());
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
        return ret;
    }
