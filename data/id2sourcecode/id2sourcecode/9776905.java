    @Override
    public LlcpPacket receiveFrom(final int nativeHandle) throws RemoteException {
        final P2PConnectionLess connectionLess = this.connections.get(nativeHandle);
        if (connectionLess == null) {
            return null;
        }
        final byte[] buffer = new byte[1024];
        final ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(1024);
        try {
            int read = connectionLess.recvFrom(buffer);
            int last = read;
            while (read >= 0) {
                last = read;
                arrayOutputStream.write(buffer, 0, read);
                read = connectionLess.recvFrom(buffer);
            }
            return new LlcpPacket(buffer[last] & 0xFF, arrayOutputStream.toByteArray());
        } catch (final IOException exception) {
            if (ConnectionlessSocketLLCP.DEBUG) {
                Log.d(ConnectionlessSocketLLCP.TAG, "receive", exception);
            }
            return null;
        }
    }
