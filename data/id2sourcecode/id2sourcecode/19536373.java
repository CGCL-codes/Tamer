    protected final RandomAccessFile ensureResourceOpen() throws DbException {
        if (_raf == null) {
            try {
                this._raf = new RandomAccessFile(_file, "rw");
            } catch (FileNotFoundException e) {
                throw new DbException(e);
            }
        }
        if (_fc == null) {
            this._fc = _raf.getChannel();
        }
        return _raf;
    }
