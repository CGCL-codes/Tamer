        public SocketStreamInfo createStream() throws IOException {
            handleResolvingState();
            InetAddress ip = getIpAddress();
            if (ip == null) {
                throw new IllegalStateException("no IP address");
            }
            handleConnectingState();
            SocketFactory factory = getRvConnection().getSettings().getProxyInfo().getSocketFactory();
            int port = getConnectionPort();
            Socket socket;
            if (factory == null) {
                socket = SocketChannel.open(new InetSocketAddress(ip, port)).socket();
            } else {
                socket = factory.createSocket(ip, port);
            }
            return new SocketStreamInfo(socket.getChannel());
        }
