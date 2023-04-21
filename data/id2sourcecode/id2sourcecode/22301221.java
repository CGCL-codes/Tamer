    public OutputStream getOutputStream() {
        if (writer != null) throw new IllegalStateException("getOutputStream is called after getWriter was already called.");
        if (byteStream == null) byteStream = new ByteArrayOutputStream();
        return byteStream;
    }
