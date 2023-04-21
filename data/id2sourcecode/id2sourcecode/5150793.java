    public FileLocker(File file) throws IOException {
        _file = file.getCanonicalFile();
        if ("locked".equals(System.setProperty(propertyName(), "locked"))) {
            throw new IOException("Already locked internally");
        }
        _file.getParentFile().mkdirs();
        _file.createNewFile();
        _stream = new RandomAccessFile(_file, "rw");
        try {
            _lock = _stream.getChannel().tryLock();
        } catch (IOException e) {
            _stream.close();
            throw e;
        }
        if (_lock == null) {
            _stream.close();
            System.setProperty(propertyName(), "");
            throw new IOException("Already locked externally");
        }
    }
