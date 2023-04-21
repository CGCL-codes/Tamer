    void setListeningSocket(DatagramSocket datagramSocket) {
        if (_channel != null) {
            try {
                _channel.close();
            } catch (IOException ignored) {
            }
        }
        if (datagramSocket != null) {
            _channel = datagramSocket.getChannel();
            if (_channel == null) throw new IllegalArgumentException("No channel!");
            NIODispatcher.instance().registerReadWrite(_channel, this);
            synchronized (this) {
                _lastReportedPort = _channel.socket().getLocalPort();
                _portStable = true;
            }
        }
    }
