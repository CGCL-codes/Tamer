    public void socketReadyForAccept() {
        m_totalConnections++;
        SocketChannel socketChannel = null;
        try {
            socketChannel = getChannel().accept();
            if (socketChannel == null) {
                m_totalConnections--;
                return;
            }
            InetSocketAddress address = (InetSocketAddress) socketChannel.socket().getRemoteSocketAddress();
            if (!m_connectionAcceptor.acceptConnection(address)) {
                m_totalRefusedConnections++;
                NIOUtils.closeChannelSilently(socketChannel);
                return;
            }
            notifyNewConnection(registerSocket(socketChannel, address));
            m_totalAcceptedConnections++;
        } catch (IOException e) {
            NIOUtils.closeChannelSilently(socketChannel);
            m_totalFailedConnections++;
            notifyAcceptFailed(e);
        }
    }
