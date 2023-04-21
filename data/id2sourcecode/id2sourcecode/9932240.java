    @Override
    public long pos() throws IOException {
        return _filehandle.getChannel().position();
    }
