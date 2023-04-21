    public int read() throws IOException {
        if (_in == null) {
            _in = _url.openStream();
            if (_in == null) throw new FileNotFoundException(_url.toExternalForm());
            _in = new BufferedInputStream(_in);
        }
        return _in.read();
    }
