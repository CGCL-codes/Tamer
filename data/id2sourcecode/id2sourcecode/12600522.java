    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) throw new IllegalStateException("getWriter() has already been called for this response");
        if (stream == null) stream = createOutputStream();
        log.debug("stream is set to {} in getOutputStream", stream);
        return (stream);
    }
