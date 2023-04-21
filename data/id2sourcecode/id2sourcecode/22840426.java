    public java.io.PrintWriter getWriter() throws java.io.IOException {
        if (servletStream != null) {
            throw new IllegalStateException("Tried to create writer; output stream already exists");
        }
        if (writer == null) {
            stringWriter = new StringWriter();
            writer = new PrintWriter(stringWriter);
            if (logger.isDebugEnabled()) {
                logger.debug("Created writer for output stream " + outputStream);
            }
        }
        return writer;
    }
