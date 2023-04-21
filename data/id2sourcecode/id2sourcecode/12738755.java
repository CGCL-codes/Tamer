    public FileTrace(Socket socket) throws IOException {
        channel = new RandomAccessFile(getFile(socket), "rw").getChannel();
        print(getHeader(socket));
    }
