    @Override
    public OutputStream newOutputStream() throws IOException {
        if (isAppendMode()) throw new IOException("Append to a URL resource isn't possible. ");
        return url.openConnection().getOutputStream();
    }
