    public static void convert(DataInputStream dis, DataOutputStream dos, String newClassName) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream store = new DataOutputStream(bout);
        store.writeInt(dis.readInt());
        store.writeShort(dis.readShort());
        store.writeShort(dis.readShort());
        short n = dis.readShort();
        store.writeShort(n);
        short[] indexes = new short[n];
        int i = 1;
        while (i < n) {
            indexes[i] = (short) store.size();
            byte tag = dis.readByte();
            store.writeByte(tag);
            if (tag == 1) {
                store.writeUTF(dis.readUTF());
            } else if (tag == 7) {
                store.writeShort(dis.readShort());
            } else if (tag == 9 || tag == 10 || tag == 11) {
                store.writeShort(dis.readShort());
                store.writeShort(dis.readShort());
            } else if (tag == 8) {
                store.writeShort(dis.readShort());
            } else if (tag == 3) {
                store.writeInt(dis.readInt());
            } else if (tag == 4) {
                store.writeFloat(dis.readFloat());
            } else if (tag == 5) {
                store.writeLong(dis.readLong());
                i = i + 1;
            } else if (tag == 6) {
                store.writeDouble(dis.readDouble());
                i = i + 1;
            } else if (tag == 12) {
                store.writeShort(dis.readShort());
                store.writeShort(dis.readShort());
            } else System.out.println("Unknown tag:" + tag + " " + i);
            i = i + 1;
        }
        store.writeShort(dis.readShort());
        int classNameIdx = dis.readShort();
        store.writeShort(classNameIdx);
        byte[] bytes = bout.toByteArray();
        int clsoffset = indexes[classNameIdx];
        if (bytes[clsoffset] == 7) {
            classNameIdx = ((bytes[clsoffset + 1] & 0xFF) << 8) | (bytes[clsoffset + 2] & 0xFF);
        }
        DataInputStream is = new DataInputStream(new ByteArrayInputStream(bytes));
        is.skip(indexes[classNameIdx]);
        is.readByte();
        dos.write(bytes, 0, indexes[classNameIdx]);
        dos.writeByte(1);
        dos.writeUTF(newClassName);
        dos.write(bytes, indexes[classNameIdx + 1], bytes.length - indexes[classNameIdx + 1]);
        copy(dis, dos);
    }
