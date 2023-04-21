    public OutputStream getOutputStream(DirectoryFile artifact, OutputStream output) throws IOException {
        return url.openConnection().getOutputStream();
    }
