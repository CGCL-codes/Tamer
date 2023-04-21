    @Override
    public DiskData build(final String db) throws IOException {
        final Prop pr = parser.prop;
        DropDB.drop(db, pr);
        pr.dbpath(db).mkdirs();
        meta = new MetaData(db, pr);
        meta.file = parser.io;
        meta.filesize = meta.file.length();
        meta.time = meta.file.date();
        int bs = IO.BLOCKSIZE;
        while (bs < meta.filesize && bs < 1 << 22) bs <<= 1;
        tout = new DataOutput(new TableOutput(meta, DATATBL));
        xout = new DataOutput(meta.file(DATATXT), bs);
        vout = new DataOutput(meta.file(DATAATV), bs);
        sout = new DataOutput(meta.file(DATATMP), bs);
        parse(db);
        close();
        final TableAccess ta = new TableDiskAccess(meta, DATATBL);
        final DataInput in = new DataInput(meta.file(DATATMP));
        for (; spos < ssize; spos++) ta.write4(in.readNum(), 8, in.readNum());
        ta.close();
        in.close();
        meta.file(DATATMP).delete();
        return new DiskData(meta, tags, atts, path, ns);
    }
