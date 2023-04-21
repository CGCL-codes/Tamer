    protected void createDBF(String DBFname, int format, boolean destroy) throws xBaseJException, IOException, SecurityException {
        jNDX = null;
        jNDXes = new Vector(1);
        jNDXID = new Vector(1);
        ffile = new File(DBFname);
        if (format != DBASEIII && format != DBASEIV && format != DBASEIII_WITH_MEMO && format != DBASEIV_WITH_MEMO && format != FOXPRO_WITH_MEMO) throw new xBaseJException("Invalid format specified");
        if (destroy == false) if (ffile.exists()) throw new xBaseJException("File exists, can't destroy");
        if (destroy == true) {
            if (ffile.exists()) if (ffile.delete() == false) throw new xBaseJException("Can't delete old DBF file");
            ffile = new File(DBFname);
        }
        FileOutputStream tFOS = new FileOutputStream(ffile);
        tFOS.close();
        file = new RandomAccessFile(DBFname, "rw");
        dosname = DBFname;
        channel = file.getChannel();
        buffer = ByteBuffer.allocateDirect(lrecl + 1);
        fld_root = new Vector(0);
        if (format == DBASEIV || format == DBASEIV_WITH_MEMO) MDX_exist = 1;
        boolean memoExists = (format == DBASEIII_WITH_MEMO || format == DBASEIV_WITH_MEMO || format == FOXPRO_WITH_MEMO);
        db_offset(format, memoExists);
        update_dbhead();
        file.writeByte(13);
        file.writeByte(26);
        if (MDX_exist == 1) MDXfile = new MDXFile(DBFname, this, destroy);
    }
