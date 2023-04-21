    @Override
    public SocketChannel shutdownOutput() throws IOException {
        synchronized (stateLock) {
            if (!isOpen()) throw new ClosedChannelException();
            if (!isConnected()) throw new NotYetConnectedException();
            if (isOutputOpen) {
                Net.shutdown(fd, Net.SHUT_WR);
                if (writerThread != 0) NativeThread.signal(writerThread);
                isOutputOpen = false;
            }
            return this;
        }
    }
