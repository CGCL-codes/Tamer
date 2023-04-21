    private void startDownload() {
        try {
            socketChannel = SocketChannel.open();
            FileChannel fileChannel = file.getChannel();
            socketChannel.connect(new InetSocketAddress(listenInetAddress, listenPort));
            ByteBuffer fileBuffer = file.getFileByteBuffer();
            ByteBuffer ackBuffer = ByteBuffer.allocate(8);
            int bytesRead = 0;
            while (!file.allBytesTransferred()) {
                bytesRead = socketChannel.read(fileBuffer);
                if (bytesRead == 0) {
                    interrupted();
                    return;
                }
                fileBuffer.flip();
                fileChannel.write(fileBuffer);
                if (fileBuffer.hasRemaining()) {
                    fileBuffer.compact();
                } else {
                    fileBuffer.clear();
                }
                resetActivityTimer();
                addBytesProcessed(bytesRead);
                if (!turboMode) {
                    ackBuffer.putLong(file.currentFilePosition());
                    ackBuffer.flip();
                    socketChannel.write(ackBuffer);
                }
            }
            finished();
        } catch (Exception e) {
            if (e.getMessage().indexOf("refused") > 0) {
                userInfo.getConnection().getListener().error(ReplyCode.BABBLE_DCC_CONNECTION_REFUSED, "Connection refused by remote user.");
            } else {
                userInfo.getConnection().getListener().error(ReplyCode.BABBLE_CONNECTION_FAILED, "Unknown socket error:" + e.getMessage());
            }
            interrupted();
        }
    }
