    public ConnectionHandlerLocal(Socket s) throws IOException {
        _socket = s;
        _out = new PrintStream(s.getOutputStream());
        _reader = new ReaderThread(this, _socket);
        _writer = new WriterThread(_out);
        _writer.start();
    }
