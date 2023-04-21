    public ChannelImpl connect() {
        synchronized (this) {
            String connection_id = "/meta/connections/" + getId();
            _connection = (ChannelImpl) _bayeux.getChannel(connection_id, true);
            _connection.subscribe(this);
            return _connection;
        }
    }
