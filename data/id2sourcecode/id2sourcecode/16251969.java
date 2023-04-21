    protected URLConnection openConnection(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        if (_basicAuth != null) conn.setRequestProperty("Authorization", _basicAuth); else if (_user != null && _password != null) {
            _basicAuth = "Basic " + base64(_user + ":" + _password);
            conn.setRequestProperty("Authorization", _basicAuth);
        }
        return conn;
    }
