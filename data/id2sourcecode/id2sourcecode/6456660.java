    public final int getSelectionOps() {
        int rv = 0;
        if (getChannel().isConnected()) {
            if (hasReadOp()) {
                rv |= SelectionKey.OP_READ;
            }
            if (toWrite > 0 || hasWriteOp()) {
                rv |= SelectionKey.OP_WRITE;
            }
        } else {
            rv = SelectionKey.OP_CONNECT;
        }
        return rv;
    }
