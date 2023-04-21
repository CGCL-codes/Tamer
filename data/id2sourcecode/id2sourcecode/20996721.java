    @Override
    public void processRequest(Request rq) {
        Chunk chunk = (Chunk) rq.getRequestMessage();
        int length = (int) (chunk.getEnd() - chunk.getStart());
        Logging.logMessage(Logging.LEVEL_INFO, this, "%s request received from %s", chunk.toString(), rq.getSenderAddress().toString());
        FileChannel channel = null;
        ReusableBuffer payload = null;
        try {
            channel = new FileInputStream(chunk.getFileName()).getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(length);
            if (channel.read(buffer, chunk.getStart()) != length) throw new Exception();
            buffer.flip();
            payload = new ReusableBuffer(buffer);
            rq.sendSuccess(ErrorCodeResponse.getDefaultInstance(), payload);
        } catch (Exception e) {
            if (payload != null) BufferPool.free(payload);
            if (e.getMessage() == null) {
                Logging.logError(Logging.LEVEL_WARN, this, e);
            } else {
                Logging.logMessage(Logging.LEVEL_INFO, this, "The requested chunk (%s) is not" + " available anymore, because: %s", chunk.toString(), e.getMessage());
            }
            rq.sendSuccess(ErrorCodeResponse.newBuilder().setErrorCode(ErrorCode.FILE_UNAVAILABLE).build());
        } finally {
            try {
                if (channel != null) channel.close();
            } catch (IOException e) {
            }
        }
    }
