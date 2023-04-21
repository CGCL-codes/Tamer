    private void updateRemoteDetails() {
        if ((null == _remoteHost) || (null == _remoteAddr) || (_remotePort <= 0)) {
            if (isOpen()) {
                final SocketChannel channel = getChannel();
                final Socket sock = (null == channel) ? null : channel.socket();
                final InetSocketAddress sockAddress = (null == sock) ? null : (InetSocketAddress) sock.getRemoteSocketAddress();
                if (null == _remoteAddr) _remoteAddr = resolveRemoteString(NetUtil.getRemoteHostAddress(sockAddress), "?.?.?.?");
                if (null == _remoteHost) _remoteHost = resolveRemoteString(sockAddress.getHostName(), "???");
                if (_remotePort <= 0) {
                    if ((_remotePort = sockAddress.getPort()) <= 0) _remotePort = Integer.MAX_VALUE;
                }
            }
        }
    }
