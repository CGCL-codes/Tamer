    public int handshake(boolean read, boolean write) throws IOException {
        if (initHandshakeComplete) return 0;
        if (!flush(netOutBuffer)) return SelectionKey.OP_WRITE;
        SSLEngineResult handshake = null;
        while (!initHandshakeComplete) {
            switch(initHandshakeStatus) {
                case NOT_HANDSHAKING:
                    {
                        throw new IOException("NOT_HANDSHAKING during handshake");
                    }
                case FINISHED:
                    {
                        initHandshakeComplete = !netOutBuffer.hasRemaining();
                        return initHandshakeComplete ? 0 : SelectionKey.OP_WRITE;
                    }
                case NEED_WRAP:
                    {
                        handshake = handshakeWrap(write);
                        if (handshake.getStatus() == Status.OK) {
                            if (initHandshakeStatus == HandshakeStatus.NEED_TASK) initHandshakeStatus = tasks();
                        } else {
                            throw new IOException("Unexpected status:" + handshake.getStatus() + " during handshake WRAP.");
                        }
                        if (initHandshakeStatus != HandshakeStatus.NEED_UNWRAP || (!flush(netOutBuffer))) {
                            return SelectionKey.OP_WRITE;
                        }
                    }
                case NEED_UNWRAP:
                    {
                        handshake = handshakeUnwrap(read);
                        if (handshake.getStatus() == Status.OK) {
                            if (initHandshakeStatus == HandshakeStatus.NEED_TASK) initHandshakeStatus = tasks();
                        } else if (handshake.getStatus() == Status.BUFFER_UNDERFLOW) {
                            return SelectionKey.OP_READ;
                        } else {
                            throw new IOException("Invalid handshake status:" + initHandshakeStatus + " during handshake UNWRAP.");
                        }
                        break;
                    }
                case NEED_TASK:
                    {
                        initHandshakeStatus = tasks();
                        break;
                    }
                default:
                    throw new IllegalStateException("Invalid handshake status:" + initHandshakeStatus);
            }
        }
        return initHandshakeComplete ? 0 : (SelectionKey.OP_WRITE | SelectionKey.OP_READ);
    }
