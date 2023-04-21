    public String toString() {
        ByteArrayOutputStream bbuf = new ByteArrayOutputStream();
        DataOutputStream dbuf = new DataOutputStream(bbuf);
        try {
            dbuf.writeBytes("attribute tag: 0x" + Integer.toHexString(atag) + "(" + getTagName(atag) + ")" + "\n");
            dbuf.writeBytes("attribute name: " + new String(aname) + "\n");
            switch(atag) {
                case TAG_INTEGER:
                case TAG_BOOLEAN:
                case TAG_ENUM:
                    for (int ii = avalue.size(), i = 0; i < ii; i++) {
                        Integer v = (Integer) avalue.get(i);
                        dbuf.writeBytes(v.toString() + "\n");
                    }
                    break;
                case TAG_OCTETSTRINGUNSPECIFIEDFORMAT:
                case TAG_RESOLUTION:
                case TAG_TEXTWITHLANGUAGE:
                case TAG_NAMEWITHLANGUAGE:
                    for (int ii = avalue.size(), i = 0; i < ii; i++) {
                        byte[] bv = (byte[]) avalue.get(i);
                        dbuf.writeBytes(new String(bv) + "\n");
                    }
                    break;
                case TAG_DATETIME:
                    for (int ii = avalue.size(), i = 0; i < ii; i++) {
                        byte[] bv = (byte[]) avalue.get(i);
                        ByteArrayInputStream bi = new ByteArrayInputStream(bv);
                        DataInputStream di = new DataInputStream(bi);
                        dbuf.writeBytes(Integer.toString(di.readShort()) + "-" + Integer.toString(di.readByte()) + "-" + Integer.toString(di.readByte()) + "," + Integer.toString(di.readByte()) + ":" + Integer.toString(di.readByte()) + ":" + Integer.toString(di.readByte()) + "." + Integer.toString(di.readByte()) + "," + (char) di.readByte() + Integer.toString(di.readByte()) + ":" + Integer.toString(di.readByte()) + "\n");
                    }
                    break;
                case TAG_RANGEOFINTEGER:
                    for (int ii = avalue.size(), i = 0; i < ii; i++) {
                        byte[] bv = (byte[]) avalue.get(i);
                        ByteArrayInputStream bi = new ByteArrayInputStream(bv);
                        DataInputStream di = new DataInputStream(bi);
                        dbuf.writeBytes(Integer.toString(di.readInt()) + "..." + Integer.toString(di.readInt()) + "\n");
                    }
                    break;
                case TAG_TEXTWITHOUTLANGUAGE:
                case TAG_NAMEWITHOUTLANGUAGE:
                case TAG_KEYWORD:
                case TAG_URI:
                case TAG_URISCHEME:
                case TAG_CHARSET:
                case TAG_NATURAL_LANGUAGE:
                case TAG_MIMEMEDIATYPE:
                    for (int ii = avalue.size(), i = 0; i < ii; i++) {
                        byte[] bv = (byte[]) avalue.get(i);
                        dbuf.writeBytes(new String(bv) + "\n");
                    }
                    break;
                default:
                    for (int ii = avalue.size(), i = 0; i < ii; i++) {
                    }
                    break;
            }
            dbuf.flush();
            dbuf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bbuf.toString();
    }
