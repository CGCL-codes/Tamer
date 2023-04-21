    private synchronized void init(int _table_size) {
        _meta = new File(_base.getAbsolutePath() + ".mta");
        _index = new File(_base.getAbsolutePath() + ".idx");
        _dbase = new File(_base.getAbsolutePath() + ".dbm");
        String attr = (_readonly) ? R_ATTR : _attr;
        if (_meta.exists()) {
            try {
                _f_meta = new NDbmRandomAccessFile(_meta, attr);
                _f_idx = new NDbmRandomAccessFile(_index, attr);
                _f_dbase = new NDbmRandomAccessFile(_dbase, attr);
                _c_meta = _f_meta.getChannel();
                int v = _f_meta.readInt();
                if (!versionSupported(v)) {
                    logger.fatal(String.format("Unexpected: version of NDbm format = %d, expected %d", v, FORMAT_VERSION));
                }
                _closed = false;
            } catch (Exception E) {
                logger.fatal(E);
            }
        } else if (!_readonly) {
            try {
                _f_meta = new NDbmRandomAccessFile(_meta, RW_ATTR);
                _f_idx = new NDbmRandomAccessFile(_index, RW_ATTR);
                _f_dbase = new NDbmRandomAccessFile(_dbase, RW_ATTR);
                _f_meta.writeInt(FORMAT_VERSION);
                _f_meta.writeInt(_table_size);
                int first_empty_node = -1;
                _f_meta.writeInt(first_empty_node);
                int numOfGaps = 0;
                _f_meta.writeInt(numOfGaps);
                int collisions = 0;
                _f_meta.writeInt(collisions);
                int i;
                Bucket B = new Bucket();
                for (i = 0; i < _table_size; i++) {
                    B.writeNext(_f_idx);
                }
                _f_dbase.close();
                _f_idx.close();
                _f_meta.close();
                init();
            } catch (Exception E) {
                logger.fatal(E);
            }
        } else {
            logger.fatal("Trying to open a non existing database readonly");
        }
    }
