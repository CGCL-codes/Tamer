    public void addField(Field aField[]) throws xBaseJException, IOException {
        if (aField.length == 0) throw new xBaseJException("No Fields in array to add");
        if ((version == DBASEIII && MDX_exist == 0) || (version == DBASEIII_WITH_MEMO)) {
            if ((fldcount + aField.length) > 128) throw new xBaseJException("Number of fields exceed limit of 128.  New Field count is " + (fldcount + aField.length));
        } else {
            if ((fldcount + aField.length) > 255) throw new xBaseJException("Number of fields exceed limit of 255.  New Field count is " + (fldcount + aField.length));
        }
        int i, j;
        Field tField;
        boolean oldMemo = false;
        for (j = 0; j < aField.length; j++) {
            for (i = 1; i <= fldcount; i++) {
                tField = getField(i);
                if (tField.isMemoField() || tField.isPictureField()) oldMemo = true;
                if (aField[j].getName().equalsIgnoreCase(tField.getName())) throw new xBaseJException("Field: " + aField[j].getName() + " already exists.");
            }
        }
        short newRecl = lrecl;
        boolean newMemo = false;
        for (j = 1; j <= aField.length; j++) {
            newRecl += aField[j - 1].getLength();
            if ((dbtobj == null) && ((aField[j - 1] instanceof MemoField) || (aField[j - 1] instanceof PictureField))) newMemo = true;
            if (aField[j - 1] instanceof PictureField) version = FOXPRO_WITH_MEMO; else if ((aField[j - 1] instanceof MemoField) && (((MemoField) aField[j - 1]).isFoxPro())) version = FOXPRO_WITH_MEMO;
        }
        String ignoreDBFLength = Util.getxBaseJProperty("ignoreDBFLengthCheck");
        if (ignoreDBFLength != null && (ignoreDBFLength.toLowerCase().compareTo("true") == 0 || ignoreDBFLength.toLowerCase().compareTo("yes") == 0)) ; else if (newRecl > 4000) throw new xBaseJException("Record length of 4000 exceeded.  New calculated length is " + newRecl);
        boolean createTemp = false;
        DBF tempDBF = null;
        String newName = "";
        if (fldcount > 0) createTemp = true;
        if (createTemp) {
            File f = File.createTempFile("org.xBaseJ", ffile.getName());
            newName = f.getAbsolutePath();
            f.delete();
            int format = version;
            if ((format == DBASEIII) && (MDX_exist == 1)) format = DBASEIV;
            tempDBF = new DBF(newName, format, true);
            tempDBF.version = (byte) format;
            tempDBF.MDX_exist = MDX_exist;
        }
        if (newMemo) {
            if (createTemp) {
                if ((version == DBASEIII || version == DBASEIII_WITH_MEMO) && (MDX_exist == 0)) tempDBF.dbtobj = new DBT_iii(this, newName, true); else if (version == FOXPRO_WITH_MEMO) tempDBF.dbtobj = new DBT_fpt(this, newName, true); else tempDBF.dbtobj = new DBT_iv(this, newName, true);
            } else {
                if ((version == DBASEIII || version == DBASEIII_WITH_MEMO) && (MDX_exist == 0)) dbtobj = new DBT_iii(this, dosname, true); else if (version == FOXPRO_WITH_MEMO) dbtobj = new DBT_fpt(this, dosname, true); else dbtobj = new DBT_iv(this, dosname, true);
            }
        } else if (createTemp && oldMemo) {
            if ((version == DBASEIII || version == DBASEIII_WITH_MEMO) && (MDX_exist == 0)) tempDBF.dbtobj = new DBT_iii(this, newName, true); else if (version == FOXPRO_WITH_MEMO) tempDBF.dbtobj = new DBT_fpt(this, newName, true); else tempDBF.dbtobj = new DBT_iv(this, newName, true);
        }
        if (createTemp) {
            tempDBF.db_offset(version, newMemo || (dbtobj != null));
            tempDBF.update_dbhead();
            tempDBF.offset = offset;
            tempDBF.lrecl = newRecl;
            tempDBF.fldcount = fldcount;
            for (i = 1; i <= fldcount; i++) {
                try {
                    tField = (Field) getField(i).clone();
                } catch (CloneNotSupportedException e) {
                    throw new xBaseJException("Clone not supported logic error");
                }
                if (tField.isMemoField()) ((MemoField) tField).setDBTObj(tempDBF.dbtobj);
                if (tField.isPictureField()) ((PictureField) tField).setDBTObj(tempDBF.dbtobj);
                tField.setBuffer(tempDBF.buffer);
                tempDBF.fld_root.addElement(tField);
                tempDBF.write_Field_header(tField);
            }
            for (i = 0; i < aField.length; i++) {
                aField[i].setBuffer(tempDBF.buffer);
                tempDBF.fld_root.addElement(aField[i]);
                tempDBF.write_Field_header(aField[i]);
                tField = (Field) aField[i];
                if (tField.isMemoField()) ((MemoField) tField).setDBTObj(tempDBF.dbtobj);
                if (tField.isPictureField()) ((PictureField) tField).setDBTObj(tempDBF.dbtobj);
            }
            tempDBF.file.writeByte(13);
            tempDBF.file.writeByte(26);
            tempDBF.fldcount += aField.length;
            tempDBF.offset += (aField.length * 32);
        } else {
            lrecl = newRecl;
            int savefldcnt = fldcount;
            fldcount += aField.length;
            offset += (32 * aField.length);
            if (newMemo) {
                if (dbtobj instanceof DBT_iii) version = DBASEIII_WITH_MEMO; else if (dbtobj instanceof DBT_iv) version = DBASEIV_WITH_MEMO; else if (dbtobj instanceof DBT_fpt) version = FOXPRO_WITH_MEMO;
            }
            channel = file.getChannel();
            buffer = ByteBuffer.allocateDirect(lrecl + 1);
            update_dbhead();
            for (i = 1; i <= savefldcnt; i++) {
                tField = (Field) getField(i);
                if (tField.isMemoField()) ((MemoField) tField).setDBTObj(dbtobj);
                if (tField.isPictureField()) ((PictureField) tField).setDBTObj(tempDBF.dbtobj);
                write_Field_header(tField);
            }
            for (i = 0; i < aField.length; i++) {
                aField[i].setBuffer(buffer);
                tField = (Field) aField[i];
                if (tField.isMemoField()) ((MemoField) tField).setDBTObj(dbtobj);
                if (tField.isPictureField()) {
                    ((PictureField) tField).setDBTObj(dbtobj);
                }
                fld_root.addElement(aField[i]);
                write_Field_header(aField[i]);
            }
            file.writeByte(13);
            file.writeByte(26);
            return;
        }
        tempDBF.update_dbhead();
        tempDBF.close();
        tempDBF = new DBF(newName);
        for (j = 1; j <= count; j++) {
            Field old1;
            Field new1;
            gotoRecord(j);
            for (i = 1; i <= fldcount; i++) {
                old1 = getField(i);
                new1 = tempDBF.getField(i);
                new1.put(old1.get());
            }
            for (i = 0; i < aField.length; i++) {
                new1 = aField[i];
                new1.put("");
            }
            tempDBF.write();
        }
        tempDBF.update_dbhead();
        file.close();
        ffile.delete();
        if (dbtobj != null) {
            dbtobj.file.close();
            dbtobj.thefile.delete();
        }
        if (tempDBF.dbtobj != null) {
            tempDBF.dbtobj.file.close();
            if (newName != null && newName.length() > 4) {
                String tempMDXFilename = newName.substring(0, newName.length() - 4) + ".mdx";
                File tempMDXFile = new File(tempMDXFilename);
                if (tempMDXFile.exists()) {
                    tempMDXFile.deleteOnExit();
                }
            }
            tempDBF.dbtobj.rename(dosname);
            if ((version == DBASEIII || version == DBASEIII_WITH_MEMO) && (MDX_exist == 0)) {
                if (dosname.endsWith("dbf")) dbtobj = new DBT_iii(this, readonly); else dbtobj = new DBT_iii(this, dosname, true);
            } else if (version == FOXPRO_WITH_MEMO) {
                if (dosname.endsWith("dbf")) dbtobj = new DBT_fpt(this, readonly); else dbtobj = new DBT_fpt(this, dosname, true);
            } else {
                if (dosname.endsWith("dbf")) dbtobj = new DBT_iv(this, readonly); else dbtobj = new DBT_iv(this, dosname, true);
            }
        }
        tempDBF.renameTo(dosname);
        buffer = ByteBuffer.allocateDirect(tempDBF.buffer.capacity());
        tempDBF = null;
        ffile = new File(dosname);
        file = new RandomAccessFile(dosname, "rw");
        channel = file.getChannel();
        for (i = 0; i < aField.length; i++) {
            aField[i].setBuffer(buffer);
            fld_root.addElement(aField[i]);
        }
        read_dbhead();
        fldcount = (short) ((offset - 1) / 32 - 1);
        for (i = 1; i <= fldcount; i++) {
            tField = (Field) getField(i);
            tField.setBuffer(buffer);
            if (tField.isMemoField()) ((MemoField) tField).setDBTObj(dbtobj);
            if (tField.isPictureField()) ((PictureField) tField).setDBTObj(dbtobj);
        }
    }
