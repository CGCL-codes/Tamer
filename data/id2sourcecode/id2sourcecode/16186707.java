    private void writeIntoFile(final FtpUserSession session, final OutputStream writter) {
        InputStream clientStream = session.getDataConnection().getInputStream();
        byte[] buffer = new byte[FtpConstants.bufferSize];
        long byteTransferred = 0;
        session.getTransferStatus().setTransferInProgress(true);
        try {
            while (true) {
                int read = clientStream.read(buffer, 0, buffer.length);
                if (read == -1) break;
                writter.write(buffer, 0, read);
                byteTransferred += read;
                session.getTransferStatus().setByteTransfered(byteTransferred);
            }
            writter.close();
        } catch (Exception e) {
            session.getControlConnection().scheduleSend(FtpReply.getFtpReply(451));
            session.getDataConnection().close();
            logger.error("Exception while writtinginto local file" + e);
            return;
        }
        session.getTransferStatus().setTransferInProgress(false);
        session.getDataConnection().close();
        session.getControlConnection().scheduleSend(FtpReply.getFtpReply(226));
        if (FtpLogger.debug) {
            logger.debug("Data transfer complete for user session" + session.getSessionKey());
        }
    }
