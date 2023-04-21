    public JavaSeisWriter(String directory) {
        super(directory);
        _io_mode = Seisio.MODE_READ_WRITE;
        _read_write = true;
        if (!_opened_before) _descr = new JavaSeisDescriptor();
    }
