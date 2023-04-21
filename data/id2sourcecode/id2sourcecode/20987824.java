    public HttpConnectionHandler(String readURL, String writeURL) throws IOException {
        _readURL = readURL;
        _writeURL = writeURL;
        URL read = new URL(_readURL);
        URLConnection urlConn = read.openConnection();
        urlConn.setUseCaches(false);
        _in = new DataInputStream(urlConn.getInputStream());
        _id = _in.readLine();
        _linesRead++;
        _reader = new Thread(this);
        _writer = new HttpWriterThread(_id, _writeURL);
    }
