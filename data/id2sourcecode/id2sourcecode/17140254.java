    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) throw new IllegalStateException("getWriter() has already been called for this response");
        if (stream == null) stream = createOutputStream();
        if (debug > 1) {
            System.out.println("stream is set to " + stream + " in getOutputStream");
        }
        return (stream);
    }
