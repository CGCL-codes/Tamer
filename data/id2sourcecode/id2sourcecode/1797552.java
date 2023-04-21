    private AbifTag createAbifTag(RandomAccessFile file, DirEntry entry) throws IOException {
        int dataSize = entry.getDatasize();
        int numElem = entry.getNumelements();
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bas);
        ByteArrayInputStream bis;
        DataInputStream dis;
        if (dataSize <= 4) {
            int d = entry.getDataoffset();
            dos.writeInt(d);
        } else {
            file.seek(entry.getDataoffset());
            for (int i = 0; i < dataSize; i++) {
                dos.writeByte(file.readByte());
            }
        }
        bis = new ByteArrayInputStream(bas.toByteArray());
        dis = new DataInputStream(bis);
        Object[] data = new Object[numElem];
        AbifTag.TagType type = null;
        switch(entry.getType()) {
            case BYTE:
                for (int i = 0; i < numElem; i++) {
                    data[i] = (int) (dis.readUnsignedByte());
                }
                type = AbifTag.TagType.INT;
                break;
            case WORD:
                for (int i = 0; i < numElem; i++) {
                    data[i] = (int) (dis.readUnsignedShort());
                }
                type = AbifTag.TagType.INT;
                break;
            case SHORT:
                for (int i = 0; i < numElem; i++) {
                    data[i] = (int) (dis.readShort());
                }
                type = AbifTag.TagType.INT;
                break;
            case LONG:
                for (int i = 0; i < numElem; i++) {
                    data[i] = (int) (dis.readInt());
                }
                type = AbifTag.TagType.INT;
                break;
            case CHAR:
                for (int i = 0; i < numElem; i++) {
                    data[i] = (char) (dis.readUnsignedByte());
                }
                type = AbifTag.TagType.CHAR;
                break;
            case FLOAT:
                for (int i = 0; i < numElem; i++) {
                    data[i] = (float) (dis.readFloat());
                }
                type = AbifTag.TagType.FLOAT;
                break;
            case DOUBLE:
                for (int i = 0; i < numElem; i++) {
                    data[i] = (double) (dis.readDouble());
                }
                type = AbifTag.TagType.DOUBLE;
                break;
            case DATE:
                for (int i = 0; i < numElem; i++) {
                    data[i] = new AbifTag.Date(dis.readShort(), dis.readUnsignedByte(), dis.readUnsignedByte());
                }
                type = AbifTag.TagType.DATE;
                break;
            case TIME:
                for (int i = 0; i < numElem; i++) {
                    data[i] = new AbifTag.Time(dis.readUnsignedByte(), dis.readUnsignedByte(), dis.readUnsignedByte(), dis.readUnsignedByte());
                }
                type = AbifTag.TagType.TIME;
                break;
            case PSTRING:
                char[] arr = new char[entry.getNumelements()];
                dis.readUnsignedByte();
                for (int i = 0; i < numElem - 1; i++) {
                    arr[i] = (char) (dis.readUnsignedByte());
                }
                arr[numElem - 1] = '\0';
                data[0] = String.valueOf(arr);
                type = AbifTag.TagType.STRING;
                break;
            case CSTRING:
                char[] arr2 = new char[numElem];
                for (int i = 0; i < numElem - 1; i++) {
                    arr2[i] = (char) (dis.readUnsignedByte());
                }
                data[0] = String.valueOf(arr2);
                type = AbifTag.TagType.STRING;
                break;
        }
        return new AbifTag(entry, data, type);
    }
