    public MMOConnection(SelectorThread<T> selectorThread, Socket socket, SelectionKey key) {
        _selectorThread = selectorThread;
        _selectionKey = key;
        _socket = socket;
        _writableByteChannel = socket.getChannel();
        _readableByteChannel = socket.getChannel();
        _sendQueue = new ArrayDeque<SendablePacket<T>>();
        _recvQueue = new MMOExecutableQueue<T>(selectorThread.getExecutor());
    }
