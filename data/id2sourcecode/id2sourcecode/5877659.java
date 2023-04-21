    public void genRS() {
        if (iCoordinatesTOsFilenames == null || iCoordinatesTOsFilenames.isEmpty()) {
            return;
        }
        try {
            for (int crrFile = 1; crrFile <= ChartSettings.getLastFileIndex(); crrFile++) {
                DataInputStream in = openInputStream(istrSupplier, "data/dataFile_" + crrFile + ".dat");
                RecordStore rs = RecordStore.openRecordStore("dataFile_" + crrFile, true, RecordStore.AUTHMODE_ANY, true);
                try {
                    while (true) {
                        ByteArrayOutputStream bout = new ByteArrayOutputStream();
                        DataOutputStream out = new DataOutputStream(bout);
                        int size = in.readShort();
                        out.writeShort(size);
                        for (int crrStar = 0; crrStar < size; crrStar++) {
                            out.writeFloat(in.readFloat());
                            out.writeFloat(in.readFloat());
                            out.writeShort(in.readShort());
                        }
                        size = in.readShort();
                        out.writeShort(size);
                        for (int crrStar = 0; crrStar < size; crrStar++) {
                            out.writeFloat(in.readFloat());
                            out.writeFloat(in.readFloat());
                            out.writeShort(in.readShort());
                            out.writeByte(in.readByte());
                            out.writeFloat(in.readFloat());
                            out.writeUTF(in.readUTF());
                        }
                        byte[] bt = bout.toByteArray();
                        rs.addRecord(bt, 0, bt.length);
                    }
                } catch (EOFException e) {
                }
                in.close();
                rs.closeRecordStore();
            }
        } catch (RecordStoreFullException e) {
            e.printStackTrace();
        } catch (RecordStoreNotFoundException e) {
            e.printStackTrace();
        } catch (RecordStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
