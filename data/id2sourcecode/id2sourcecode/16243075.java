    public OutputStream getPortletOutputStream() throws IOException {
        if (writer != null) throw new IllegalStateException("Portlet writer has already been requested.");
        if (buffer == null) {
            buffer = new CommitableBufferedOutputStream(out, bufferSize);
        }
        return buffer;
    }
