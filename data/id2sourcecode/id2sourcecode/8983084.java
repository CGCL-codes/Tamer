    public PrintWriter getWriter() throws UnsupportedEncodingException {
        if (_servletStream != null) throw new IllegalStateException("Tried to create writer; output stream already exists");
        if (_writer == null) {
            _outputStream = new ByteArrayOutputStream();
            _writer = new PrintWriter(new OutputStreamWriter(_outputStream, getCharacterEncoding()));
        }
        return _writer;
    }
