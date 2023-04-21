    private long write(final ByteBuffer[] byteBuffers) throws IOException {
        long bytesToWrite = 0;
        for (ByteBuffer byteBuffer : byteBuffers) {
            bytesToWrite += byteBuffer.remaining();
        }
        if (bytesToWrite == 0L) {
            return 0L;
        }
        long bytesWritten = 0;
        Selector writeSelector = null;
        SelectionKey wKey = null;
        int attempts = 1;
        try {
            do {
                long wroteBytes;
                do {
                    wroteBytes = socketChannel.write(byteBuffers);
                    bytesWritten += wroteBytes;
                    if (wroteBytes < 0) {
                        throw new EOFException();
                    }
                    if (Logging.SHOW_FINER && LOG.isLoggable(Level.FINER)) {
                        LOG.finer(MessageFormat.format("Wrote {0} bytes", wroteBytes));
                    }
                } while (wroteBytes != 0);
                if (bytesWritten == bytesToWrite) {
                    break;
                }
                attempts++;
                if (attempts > MAX_WRITE_ATTEMPTS) {
                    throw new IOException(MessageFormat.format("Max write attempts ({0}) exceeded ({1})", attempts, MAX_WRITE_ATTEMPTS));
                }
                if (writeSelector == null) {
                    try {
                        writeSelector = tcpTransport.getSelector();
                    } catch (InterruptedException woken) {
                        InterruptedIOException incompleteIO = new InterruptedIOException("Interrupted while acquiring write selector.");
                        incompleteIO.initCause(woken);
                        incompleteIO.bytesTransferred = (int) Math.min(bytesWritten, Integer.MAX_VALUE);
                        throw incompleteIO;
                    }
                    if (writeSelector == null) {
                        continue;
                    }
                    wKey = socketChannel.register(writeSelector, SelectionKey.OP_WRITE);
                }
                int ready = writeSelector.select(TcpTransport.connectionTimeOut);
                if (ready == 0) {
                    throw new SocketTimeoutException("Timeout during socket write");
                } else {
                    attempts--;
                }
            } while (attempts <= MAX_WRITE_ATTEMPTS);
        } finally {
            if (wKey != null) {
                wKey.cancel();
                wKey = null;
            }
            if (writeSelector != null) {
                writeSelector.selectNow();
                tcpTransport.returnSelector(writeSelector);
            }
        }
        return bytesWritten;
    }
