    public PrintWriter getWriter() throws IOException, IllegalStateException {
        if (hasBinaryContent()) {
            throw new IllegalStateException("A ServletOutputStream is already being used to " + "write the body.");
        }
        if (!hasTextContent()) {
            innerTextWriter = new CharArrayWriter();
            outerTextWriter = new PrintWriter(innerTextWriter);
        }
        return outerTextWriter;
    }
