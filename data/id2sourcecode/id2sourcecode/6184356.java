    private void editProfileName(RecordStore rs, int idx, String newname) throws Exception {
        int i, profiles;
        DataInputStream dis;
        DataOutputStream dos;
        ByteArrayOutputStream baos;
        boolean createnew = false;
        byte[] temp = rs.getRecord(1);
        dis = new DataInputStream(new ByteArrayInputStream(temp));
        baos = new ByteArrayOutputStream();
        dos = new DataOutputStream(baos);
        profiles = dis.readInt();
        if (newname == null && idx >= 0 && idx < profiles) {
            profiles--;
            ;
            idxarray = new int[profiles];
        } else if (idx < 0 || idx >= profiles) {
            profiles++;
            idxarray = new int[profiles];
            createnew = true;
        } else if (newname == null) {
            return;
        }
        dos.writeInt(profiles);
        for (i = 0; i < profiles; i++) {
            if (i == profiles - 1 && createnew) break;
            if (i == idx) {
                if (newname != null) {
                    dis.readUTF();
                    dos.writeUTF(newname);
                    idxarray[i] = dis.readInt();
                    dos.writeInt(idxarray[i]);
                    continue;
                } else {
                    dis.readUTF();
                    dis.readInt();
                }
            }
            dos.writeUTF(dis.readUTF());
            idxarray[i] = dis.readInt();
            dos.writeInt(idxarray[i]);
        }
        if (createnew) {
            dos.writeUTF(newname);
            idxarray[i] = rs.getNextRecordID();
            dos.writeInt(idxarray[i]);
            rs.addRecord(new byte[0], 0, 0);
        }
        temp = baos.toByteArray();
        dis.close();
        dos.close();
        rs.setRecord(1, temp, 0, temp.length);
        if (profileidx >= idxarray.length) profileidx = idxarray.length - 1;
    }
