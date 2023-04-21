    public XMLFileOutputStream(String fileName) throws IOException {
        super(new FileOutputStream(fileName));
        ((FileOutputStream) stream).getChannel().lock();
    }
