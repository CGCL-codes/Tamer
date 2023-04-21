    public ConnectionHandlerLocal(Socket s) throws IOException {
        _socket = s;
        _out = new PrintStream(s.getOutputStream());
        _in = new DataInputStream(s.getInputStream());
        _reader = new ReaderThread(_in, this);
        _writer = new WriterThread(_out);
        _writer.start();
    }
