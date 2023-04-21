    protected OutputStream getOutputStream() throws IOException {
        return url.openConnection().getOutputStream();
    }
