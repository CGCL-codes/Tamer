    protected int engineDigest(byte[] buf, int offset, int len) throws DigestException {
        if (len < HASHSIZE) throw new DigestException();
        blockUpdate();
        while (nodes.size() > 1) {
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
                    newNodes.addElement((Object) tiger.digest());
                } else {
                    newNodes.addElement((Object) left);
                }
            }
            nodes = newNodes;
        }
        System.arraycopy(nodes.elementAt(0), 0, buf, offset, HASHSIZE);
        engineReset();
        return HASHSIZE;
    }
