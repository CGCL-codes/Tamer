    public void open() throws IOException {
        __logger.trace("Opening " + _file.getAbsolutePath());
        FileInputStream f = new FileInputStream(_file);
        _ch = f.getChannel();
        if (_ch.size() < HEADER_OFFSET) throw new IOException("Illegal header size");
        long dataBlockSize = _ch.size() - HEADER_OFFSET;
        if (dataBlockSize % 8 != 0) throw new IOException("Illegal data block size");
        _values = dataBlockSize / 8;
        __logger.trace("Data block contains " + _values + " value(s)");
        ByteBuffer bb = ByteBuffer.allocate(HEADER_OFFSET);
        int nRead;
        _cols = 0;
        nRead = _ch.read(bb);
        if (nRead <= 0) throw new IOException("Could not read header");
        bb.flip();
        _cols = (int) bb.asDoubleBuffer().get();
        bb.clear();
        if (_values % _cols != 0) throw new IOException("Illegal data block size");
        _rows = (int) _values / _cols;
        __logger.trace("Data block size (rows/cols): " + _rows + "/" + _cols);
    }
