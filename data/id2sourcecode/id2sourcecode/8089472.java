    protected int engineDigest(byte[] buf, int offset, int len) throws DigestException {
        if (len < HASHSIZE) throw new DigestException();
        if (!check) blockUpdate();
        int rows = 0;
        while (nodes.size() > 1) {
            rows++;
            if (check && rows > levelsLeft) {
                t.setSerializationByte(serialization);
                return HASHSIZE;
            }
            Vector newNodes = new Vector();
            Enumeration iter = nodes.elements();
            while (iter.hasMoreElements()) {
                byte[] left = (byte[]) iter.nextElement();
                if (iter.hasMoreElements()) {
                    byte[] right = (byte[]) iter.nextElement();
                    tiger.reset();
                    tiger.update((byte) 1);
                    tiger.update(left);
                    tiger.update(right);
                    Object dig = (Object) tiger.digest();
                    newNodes.addElement(dig);
                    if ((levelsLeft != -1) && (rows == levelsLeft)) {
                        byte[] digs = (byte[]) dig;
                        System.arraycopy(digs, 0, serialization, serializationOffset, digs.length);
                        serializationOffset += digs.length;
                    }
                } else {
                    Object obj = (Object) left;
                    newNodes.addElement(obj);
                    if ((levelsLeft != -1) && (rows == levelsLeft)) {
                        byte[] digs = (byte[]) obj;
                        System.arraycopy(digs, 0, serialization, serializationOffset, digs.length);
                        serializationOffset += digs.length;
                    }
                }
            }
            nodes = newNodes;
        }
        byte[] root = (byte[]) nodes.elementAt(0);
        System.arraycopy(root, 0, buf, offset, HASHSIZE);
        engineReset();
        if (t != null) {
            t.setHashSize(HASHSIZE);
            t.setSerialization(Base32.encode(serialization));
            t.setRoot(Base32.encode(root));
        }
        return HASHSIZE;
    }
