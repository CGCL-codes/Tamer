    public int write(ByteBuffer b) throws IOException {
        if (!getSelectorManager().isRunning()) throw new IllegalStateException(this + "ChannelManager must be running and is stopped"); else if (isClosed) {
            AsynchronousCloseException exc = new AsynchronousCloseException();
            IOException ioe = new IOException(this + "Client cannot write after the client closed the socket");
            exc.initCause(ioe);
            throw exc;
        }
        Object t = getSelectorManager().getThread();
        if (Thread.currentThread().equals(t)) {
            throw new RuntimeException(this + "You should not perform a " + "blocking write on the channelmanager thread unless you like deadlock.  " + "Use the cm threading layer, or put the code calling this write on another thread");
        }
        try {
            int remain = b.remaining();
            UtilWaitForCompletion waitWrite = new UtilWaitForCompletion(this, t);
            write(b, waitWrite, -1);
            waitWrite.waitForComplete();
            if (b.hasRemaining()) throw new RuntimeException(this + "Did not write all of the ByteBuffer out");
            return remain;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
