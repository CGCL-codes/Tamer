    protected synchronized boolean checkConnection() {
        if (_connection == null) {
            try {
                _connection = _url.openConnection();
                _connection.setUseCaches(_useCaches);
            } catch (IOException e) {
                Log.ignore(e);
            }
        }
        return _connection != null;
    }
