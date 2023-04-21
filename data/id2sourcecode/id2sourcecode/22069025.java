    public void handleRead(NetworkRequest networkRequest) {
        ByteBuffer buffer = networkRequest.getRequestBuffer();
        if (buffer == null) {
            buffer = ByteBuffer.allocateDirect(4096);
            networkRequest.storeRequestBuffer(buffer);
        }
        SocketChannel socketChannel = (SocketChannel) networkRequest.getChannel();
        int read;
        try {
            read = socketChannel.read(buffer);
        } catch (IOException e) {
            log.error("Error while reading from socket", e);
            closeSocket(socketChannel);
            return;
        }
        if (read == -1) {
            log.info("Client disconnected: " + socketChannel.socket().getInetAddress());
            closeSocket(socketChannel);
        } else if (read != 0) {
            buffer.flip();
            ByteBuffer requestBuffer;
            try {
                requestBuffer = protocolDecoder.decode(buffer);
            } catch (DecoderException e) {
                log.debug("DecoderException thrown while decoding networkRequest. Closing socket" + socketChannel, e);
                closeSocket(socketChannel);
                return;
            }
            if (requestBuffer != null) {
                log.debug("NetworkRequest decoded");
                networkRequest.storeRequestBuffer(requestBuffer);
                readServicer.read(networkRequest);
            } else {
                log.debug("NetworkRequest not decoded, continuing reading");
                buffer.position(buffer.limit());
                buffer.limit(buffer.capacity());
                networkEventThread.addChannelInterestOps(socketChannel, SelectionKey.OP_READ);
            }
        }
    }
