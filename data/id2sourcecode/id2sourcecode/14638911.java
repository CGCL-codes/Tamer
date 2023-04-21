    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) throw new IllegalStateException("getWriter() has already been called!");
        if (stream == null) stream = createOutputStream();
        return stream;
    }
