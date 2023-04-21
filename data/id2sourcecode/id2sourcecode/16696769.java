    public void handleWrite(NetworkRequest networkRequest) {
        ByteBuffer buffer = networkRequest.getResponseBuffer();
        SocketChannel socketChannel = (SocketChannel) networkRequest.getChannel();
        try {
            socketChannel.write(buffer);
            if (buffer.hasRemaining()) {
                networkEventThread.addChannelInterestOps(socketChannel, SelectionKey.OP_WRITE);
            } else {
                writeServicer.write(networkRequest);
            }
        } catch (IOException e) {
            log.error("Error while writing data", e);
            closeSocket(socketChannel);
        }
    }
