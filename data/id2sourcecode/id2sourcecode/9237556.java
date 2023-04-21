    void connect() throws IOException {
        if (_connected) {
            System.err.println("already connected...");
            return;
        }
        IOException ioexception = null;
        if (!_alwaysTunnel) {
            System.err.println("Opening socket connection to " + _host + ":" + _port);
            _responseInterface.generalMessage(Translator.getMessage("connecting.to", _host + ":" + _port));
            Socket s = null;
            try {
                if (!_attemptToTunnel) {
                    s = new Socket(_host, _port);
                } else {
                    s = _socketOpener.makeSocket(5000);
                }
                System.err.println("ChatServerLocal: connected to " + _host + ":" + _port);
                _connectionHandler = new ConnectionHandlerLocal(s);
                _connectionHandler.setListener(this);
                _connected = true;
                return;
            } catch (IOException e) {
                ioexception = e;
            }
        }
        if (_attemptToTunnel) {
            _responseInterface.generalMessage(Translator.getMessage("connecting.to", _readUrl));
            System.err.println("Opening tunnel connetion to " + _readUrl);
            _connectionHandler = new HttpConnectionHandler(_readUrl, _writeUrl);
            System.err.println("Got the connection.");
            _connectionHandler.setListener(this);
            _connected = true;
            return;
        }
        System.err.println("giving up");
        if (ioexception != null) {
            throw ioexception;
        }
    }
