    @Override
    protected void setUp() throws Exception {
        _file = IOUtil.createTempFile("TestChannelInputStream", DEFAULT_FILESIZE);
        _raf = new RandomAccessFile(_file, "rw");
        _raf.seek(0);
        _raf.write(createWalkingBytes(DEFAULT_FILESIZE));
        _raf.seek(0);
        _channel = _raf.getChannel();
    }
