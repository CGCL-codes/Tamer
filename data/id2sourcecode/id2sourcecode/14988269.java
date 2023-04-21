    protected void openDBF(String DBFname) throws IOException, xBaseJException {
        int i;
        jNDX = null;
        jNDXes = new Vector(1);
        jNDXID = new Vector(1);
        ffile = new File(DBFname);
        if (!ffile.exists() || !ffile.isFile()) {
            throw new xBaseJException("Unknown database file " + DBFname);
        }
        if (readonly) file = new RandomAccessFile(DBFname, "r"); else file = new RandomAccessFile(DBFname, "rw");
        dosname = DBFname;
        channel = file.getChannel();
        read_dbhead();
        buffer = ByteBuffer.allocateDirect(lrecl + 1);
        fldcount = (short) ((offset - 1) / 32 - 1);
        if ((version != DBASEIII) && (version != DBASEIII_WITH_MEMO) && (version != DBASEIV) && (version != DBASEIV_WITH_MEMO) && (version != FOXPRO_WITH_MEMO)) {
            String mismatch = Util.getxBaseJProperty("ignoreVersionMismatch").toLowerCase();
            if (mismatch != null && (mismatch.compareTo("true") == 0 || mismatch.compareTo("yes") == 0)) System.err.println("Wrong Version " + String.valueOf((short) version)); else throw new xBaseJException("Wrong Version " + String.valueOf((short) version));
        }
        if (version == FOXPRO_WITH_MEMO) dbtobj = new DBT_fpt(this, readonly); else if (version == DBASEIII_WITH_MEMO) dbtobj = new DBT_iii(this, readonly); else if (version == DBASEIV_WITH_MEMO) dbtobj = new DBT_iv(this, readonly);
        fld_root = new Vector(new Long(fldcount).intValue());
        for (i = 0; i < fldcount; i++) {
            fld_root.addElement(read_Field_header());
        }
        if (MDX_exist == 1) {
            try {
                if (readonly) MDXfile = new MDXFile(dosname, this, 'r'); else MDXfile = new MDXFile(dosname, this, ' ');
                for (i = 0; i < MDXfile.getAnchor().getIndexes(); i++) jNDXes.addElement(MDXfile.MDXes[i]);
            } catch (xBaseJException xbe) {
                String missing = Util.getxBaseJProperty("ignoreMissingMDX").toLowerCase();
                if (missing != null && (missing.compareTo("true") == 0 || missing.compareTo("yes") == 0)) MDX_exist = 0; else {
                    System.err.println(xbe.getMessage());
                    System.err.println("Processing continues without mdx file");
                    MDX_exist = 0;
                }
            }
        }
        try {
            file.readByte();
        } catch (EOFException IOE) {
            ;
        }
        current_record = 0;
    }
