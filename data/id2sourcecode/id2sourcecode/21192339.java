    public final InputStream getInputStream() throws IOException {
        if (isClosed() || isShutdown()) throw new IOException("Socket closed.");
        if (reader instanceof NIOInputStream) {
            InputStream stream = ((NIOInputStream) reader).getInputStream();
            NIODispatcher.instance().interestRead(getChannel(), true);
            return stream;
        } else {
            Future future = new Future() {

                private Object result;

                public void run() {
                    try {
                        NIOInputStream stream = new NIOInputStream(AbstractNBSocket.this, AbstractNBSocket.this, null).init();
                        setReadObserver(stream);
                        result = stream.getInputStream();
                    } catch (IOException iox) {
                        LOG.error("IOXed after creation", iox);
                    }
                }

                public Object getResult() {
                    return result;
                }
            };
            try {
                NIODispatcher.instance().invokeAndWait(future);
            } catch (InterruptedException ie) {
                throw (IOException) new IOException().initCause(ie);
            }
            InputStream result = (InputStream) future.getResult();
            if (result == null) throw new IOException("error constructing InputStream"); else return result;
        }
    }
