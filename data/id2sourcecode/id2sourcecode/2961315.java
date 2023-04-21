    protected synchronized boolean checkConnection() {
        if (_connection == null) {
            try {
                _connection = _url.openConnection();
            } catch (IOException e) {
                LogSupport.ignore(log, e);
            }
        }
        return _connection != null;
    }
