    private void startUpload(boolean turboMode) {
        try {
            ByteBuffer buffer = file.getFileByteBuffer();
            ByteBuffer ackBuffer = ByteBuffer.allocate(4);
            if (!turboMode) {
            }
            FileChannel fileChannel = file.getChannel();
            socketChannel = ssChannel.accept();
            if (socketChannel.isConnectionPending()) {
                socketChannel.finishConnect();
            }
            fileChannel.force(false);
            int bytesRead = 0;
            while ((bytesRead = fileChannel.read(buffer)) > 0) {
                buffer.flip();
                int bytesWritten = socketChannel.write(buffer);
                if (buffer.hasRemaining()) {
                    buffer.compact();
                } else {
                    buffer.clear();
                }
                if (!turboMode) {
                    socketChannel.read(ackBuffer);
                }
                resetActivityTimer();
                addBytesProcessed(bytesRead);
            }
            if (!turboMode) {
                ackBuffer.flip();
                byte[] bytes = new byte[4];
                ackBuffer.get(bytes);
                while (!file.acksFinished(DccUtil.dccBytesToLong(bytes))) {
                    socketChannel.read(ackBuffer);
                }
            }
            finished();
        } catch (Exception e) {
            interrupted();
        }
    }
