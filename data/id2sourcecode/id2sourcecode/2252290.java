    public static void writeData(FileStreamSupplier istrSupplier) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(5000);
            DataOutputStream os = new DataOutputStream(bos);
            os.writeInt(readerByteBuffersize);
            os.writeShort(lastFileIndex);
            os.writeShort(dsoInfoFiles);
            os.writeShort(limitMag);
            os.writeShort(limitDSOMag);
            os.writeBoolean(showDSONames);
            os.writeBoolean(showDSOs);
            os.writeShort(lightLevel);
            os.writeShort(circles.size());
            for (int i = 0; i < circles.size(); i++) {
                FinderCircleDescription desc = (FinderCircleDescription) circles.elementAt(i);
                os.writeShort(desc.getTelescopeFocalLen());
                if (desc.getTelescopeFocalLen() > 0) {
                    os.writeShort(desc.getEyepieceFocalLen());
                    os.writeShort((short) desc.getFov());
                } else {
                    os.writeFloat(desc.getFov());
                }
            }
            os.writeShort(rowSize);
            os.writeBoolean(showLines);
            os.writeBoolean(drawCircles);
            os.writeDouble(apRa);
            os.writeDouble(apDec);
            os.writeFloat(fov);
            os.writeUTF(mainFolder);
            os.writeDouble(fovMovmentDivisor);
            os.writeDouble(fovZoomMultiplier);
            byte[] bt = bos.toByteArray();
            if (isMobile) {
                RecordStore rs = RecordStore.openRecordStore("s_settings", true, RecordStore.AUTHMODE_ANY, false);
                if (rs.getNumRecords() > 0) {
                    rs.closeRecordStore();
                    RecordStore.deleteRecordStore("s_settings");
                    rs = RecordStore.openRecordStore("s_settings", true, RecordStore.AUTHMODE_ANY, false);
                }
                rs.addRecord(bt, 0, bt.length);
                rs.closeRecordStore();
                rs = null;
            } else {
                DataOutputStream out = istrSupplier.openOutputStream(mainFolder + "settings.txt");
                if (out != null) {
                    out.write(bt, 0, bt.length);
                    out.close();
                    out = null;
                }
            }
            os.close();
            bos.close();
            bos = null;
            os = null;
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
