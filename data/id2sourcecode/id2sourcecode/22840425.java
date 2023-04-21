    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("Tried to create output stream; writer already exists");
        }
        if (servletStream == null) {
            outputStream = new ByteArrayOutputStream();
            servletStream = new ServletStringOutputStream(outputStream);
            if (logger.isDebugEnabled()) {
                logger.debug("Create ServletOutputStream for outputStream " + outputStream);
            }
        }
        return servletStream;
    }
