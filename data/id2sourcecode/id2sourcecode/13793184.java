    private void copyFrom(File frombase) {
        File fromMeta = new File(frombase.getAbsolutePath() + ".mta");
        File fromIndex = new File(frombase.getAbsolutePath() + ".idx");
        File fromDbase = new File(frombase.getAbsolutePath() + ".dbm");
        try {
            _f_idx.close();
            _f_dbase.close();
            copyFile(fromIndex, _index);
            copyFile(fromDbase, _dbase);
            _f_idx = new NDbmRandomAccessFile(_index, RW_ATTR);
            _f_dbase = new NDbmRandomAccessFile(_dbase, RW_ATTR);
        } catch (Exception E) {
            logger.fatal(E);
        }
        try {
            NDbmRandomAccessFile f = new NDbmRandomAccessFile(fromMeta, R_ATTR);
            _f_meta.seek(0);
            _f_meta.writeInt(f.readInt());
            int ts = f.readInt();
            _f_meta.writeInt(ts);
            _f_meta.writeInt(f.readInt());
            _f_meta.writeInt(f.readInt());
            _f_meta.writeInt(f.readInt());
            f.close();
        } catch (Exception E) {
            logger.fatal(E);
        }
        fromIndex.delete();
        fromDbase.delete();
        fromMeta.delete();
    }
