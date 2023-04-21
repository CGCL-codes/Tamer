    public FileChannel createOutputChannel(TPath path, OutputStream outputStream) throws TIoException {
        return ((FileOutputStream) outputStream).getChannel();
    }
