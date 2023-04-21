    public ServletOutputStream getOutputStream() throws IOException {
        if (_writer != null) throw new IllegalStateException("Tried to create output stream; writer already exists");
        if (_servletStream == null) {
            _outputStream = new ByteArrayOutputStream();
            _servletStream = new ServletUnitOutputStream(_outputStream);
        }
        return _servletStream;
    }
