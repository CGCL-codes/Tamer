    public void pack() throws xBaseJException, IOException, SecurityException, CloneNotSupportedException {
        Field Fields[] = new Field[fldcount];
        int i, j;
        for (i = 1; i <= fldcount; i++) {
            Fields[i - 1] = (Field) getField(i).clone();
        }
        String parent = ffile.getParent();
        if (parent == null) parent = ".";
        File f = File.createTempFile("tempxbase", ".tmp");
        String tempname = f.getAbsolutePath();
        DBF tempDBF = new DBF(tempname, version, true);
        tempDBF.reserve = reserve;
        tempDBF.language = language;
        tempDBF.reserve2 = reserve2;
        tempDBF.MDX_exist = MDX_exist;
        tempDBF.addField(Fields);
        Field t, p;
        for (i = 1; i <= count; i++) {
            gotoRecord(i);
            if (deleted()) {
                continue;
            }
            tempDBF.buffer.position(1);
            for (j = 1; j <= fldcount; j++) {
                t = tempDBF.getField(j);
                p = getField(j);
                t.put(p.get());
            }
            tempDBF.write();
        }
        file.close();
        ffile.delete();
        tempDBF.renameTo(dosname);
        if (dbtobj != null) {
            dbtobj.file.close();
            dbtobj.thefile.delete();
        }
        if (tempDBF.dbtobj != null) {
            tempDBF.dbtobj.rename(dosname);
            dbtobj = tempDBF.dbtobj;
            Field tField;
            MemoField mField;
            for (i = 1; i <= fldcount; i++) {
                tField = getField(i);
                if (tField.isMemoField()) {
                    mField = (MemoField) tField;
                    mField.setDBTObj(dbtobj);
                }
            }
        }
        ffile = new File(dosname);
        file = new RandomAccessFile(dosname, "rw");
        channel = file.getChannel();
        read_dbhead();
        for (i = 1; i <= fldcount; i++) getField(i).setBuffer(buffer);
        Index NDXes;
        if (MDXfile != null) MDXfile.reIndex();
        if (jNDXes.size() == 0) {
            current_record = 0;
        } else {
            for (i = 1; i <= jNDXes.size(); i++) {
                NDXes = (Index) jNDXes.elementAt(i - 1);
                NDXes.reIndex();
            }
            NDXes = (Index) jNDXes.elementAt(0);
            if (count > 0) startTop();
        }
    }
