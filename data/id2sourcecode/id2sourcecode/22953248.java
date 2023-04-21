    public FileWrapper(URL url) throws OperationsException, IOException {
        if (null == url) throw new OperationsException("URL is null");
        if (null == url.toString()) throw new OperationsException("File path is null");
        fc = new RandomAccessFile(url.getFile(), "rw").getChannel();
        cacheBuffer = ByteBuffer.allocate(CACHE_SIZE);
    }
