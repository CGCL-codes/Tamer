    private void readFromFile(final FtpUserSession session, final InputStream fileReader) {
        OutputStream clientStream = session.getDataConnection().getOutputStream();
        FileInputStream fileOut = (FileInputStream) fileReader;
        FileChannel fcin = fileOut.getChannel();
        byte[] buffer = new byte[FtpConstants.bufferSize];
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        long byteTransferred = 0;
        session.getTransferStatus().setTransferInProgress(true);
        try {
            while (true) {
                byteBuffer.clear();
                int r = fcin.read(byteBuffer);
                if (r == -1) {
                    break;
                }
                byteBuffer.flip();
                clientStream.write(byteBuffer.array(), 0, byteBuffer.limit());
                byteTransferred += r;
                session.getTransferStatus().setByteTransfered(byteTransferred);
            }
            session.getTransferStatus().setTransferInProgress(false);
            fileReader.close();
        } catch (Exception e) {
            session.getControlConnection().scheduleSend(FtpReply.getFtpReply(451));
            session.getDataConnection().close();
            logger.error("Exceptio while reading from local file" + e);
            return;
        }
        session.getDataConnection().close();
        session.getControlConnection().scheduleSend(FtpReply.getFtpReply(226));
        if (FtpLogger.debug) {
            logger.debug("Data transfer complete for user session" + session.getSessionKey());
        }
    }
