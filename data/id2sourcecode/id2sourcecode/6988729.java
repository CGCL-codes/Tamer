    public WriteFuture write(Object message, SocketAddress remoteAddress) {
        if (message == null) {
            throw new IllegalArgumentException("message");
        }
        if (!getTransportMetadata().isConnectionless() && remoteAddress != null) {
            throw new UnsupportedOperationException();
        }
        if (isClosing() || !isConnected()) {
            WriteFuture future = new DefaultWriteFuture(this);
            WriteRequest request = new DefaultWriteRequest(message, future, remoteAddress);
            WriteException writeException = new WriteToClosedSessionException(request);
            future.setException(writeException);
            return future;
        }
        FileChannel openedFileChannel = null;
        try {
            if (message instanceof IoBuffer && !((IoBuffer) message).hasRemaining()) {
                throw new IllegalArgumentException("message is empty. Forgot to call flip()?");
            } else if (message instanceof FileChannel) {
                FileChannel fileChannel = (FileChannel) message;
                message = new DefaultFileRegion(fileChannel, 0, fileChannel.size());
            } else if (message instanceof File) {
                File file = (File) message;
                openedFileChannel = new FileInputStream(file).getChannel();
                message = new FilenameFileRegion(file, openedFileChannel, 0, openedFileChannel.size());
            }
        } catch (IOException e) {
            ExceptionMonitor.getInstance().exceptionCaught(e);
            return DefaultWriteFuture.newNotWrittenFuture(this, e);
        }
        WriteFuture writeFuture = new DefaultWriteFuture(this);
        WriteRequest writeRequest = new DefaultWriteRequest(message, writeFuture, remoteAddress);
        IoFilterChain filterChain = getFilterChain();
        filterChain.fireFilterWrite(writeRequest);
        if (openedFileChannel != null) {
            final FileChannel finalChannel = openedFileChannel;
            writeFuture.addListener(new IoFutureListener<WriteFuture>() {

                public void operationComplete(WriteFuture future) {
                    try {
                        finalChannel.close();
                    } catch (IOException e) {
                        ExceptionMonitor.getInstance().exceptionCaught(e);
                    }
                }
            });
        }
        return writeFuture;
    }
