        public SocketStreamInfo createStream() throws IOException {
            handleConnectingState();
            return new SocketStreamInfo(serverSocket.accept().getChannel());
        }
