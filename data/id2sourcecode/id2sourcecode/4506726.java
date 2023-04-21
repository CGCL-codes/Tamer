    List<ByteBuffer> drainWriteQueue() throws IOException {
        if (writeQueue.isEmtpy()) {
            return null;
        } else {
            return writeQueue.readAvailable();
        }
    }
