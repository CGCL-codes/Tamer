    public void connect() throws XMPPException {
        _config = new ConnectionConfiguration(_server, _port);
        _connection = new XMPPConnection(_config);
        _connection.connect();
        _connection.login(_user.get_nickname(), _user.get_password());
        set_roster(_connection.getRoster());
        _chatmanager = _connection.getChatManager();
        createMessageListener();
    }
