    public void doWrite() {
        Functions.dout(12, "write ready");
        sk.interestOps(SelectionKey.OP_READ);
        writeReady = true;
        if (sendBuffer != null) write(sendBuffer);
        writeQueued();
    }
