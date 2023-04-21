    @Nonnull
    public ServerSocketChannel createServerSocketChannel() throws IOException {
        ServerSocket socket;
        if (this.factory != null) {
            if (this.factory instanceof ServerSocketConfiguration) {
                socket = ((ServerSocketConfiguration) this.factory).createServerSocketChannel().socket();
            } else {
                socket = this.factory.createServerSocket();
                if (socket.getChannel() == null) {
                    throw new IllegalStateException("The underlying factory created a server socket without channel: " + this.factory);
                }
            }
        } else if (this.provider != null) {
            socket = this.provider.openServerSocketChannel().socket();
        } else {
            socket = ServerSocketChannel.open().socket();
        }
        configureSocket(socket);
        return socket.getChannel();
    }
