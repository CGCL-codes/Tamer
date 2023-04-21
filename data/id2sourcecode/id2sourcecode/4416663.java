    private void handleReads(SelectionKey sk, MemcachedNode qa) throws IOException {
        Operation currentOp = qa.getCurrentReadOp();
        ByteBuffer rbuf = qa.getRbuf();
        final SocketChannel channel = qa.getChannel();
        int read = channel.read(rbuf);
        while (read > 0) {
            getLogger().debug("Read %d bytes", read);
            rbuf.flip();
            while (rbuf.remaining() > 0) {
                assert currentOp != null : "No read operation";
                currentOp.readFromBuffer(rbuf);
                if (currentOp.getState() == OperationState.COMPLETE) {
                    getLogger().debug("Completed read op: %s and giving the next %d bytes", currentOp, rbuf.remaining());
                    Operation op = qa.removeCurrentReadOp();
                    assert op == currentOp : "Expected to pop " + currentOp + " got " + op;
                    currentOp = qa.getCurrentReadOp();
                }
            }
            rbuf.clear();
            read = channel.read(rbuf);
        }
    }
