    public ServletOutputStream getOutputStream() throws IOException, IllegalStateException {
        if (innerTextWriter != null) {
            throw new IllegalStateException("A PrintWriter is already " + "being used to write the body.");
        }
        if (innerBinaryStream == null) {
            innerBinaryStream = new ByteArrayOutputStream();
            outerBinaryStream = new ServletOutputStreamAdapter(innerBinaryStream);
        }
        return outerBinaryStream;
    }
