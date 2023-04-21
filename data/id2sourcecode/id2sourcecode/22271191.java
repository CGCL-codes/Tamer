    protected int engineDigest(byte[] buf, int offset, int len) throws DigestException {
        if (len < HASHSIZE) throw new DigestException();
        blockUpdate();
        while (nodes.size() > 1) {
            List newNodes = new ArrayList();
            Iterator iter = nodes.iterator();
            while (iter.hasNext()) {
                byte[] left = (byte[]) iter.next();
                if (iter.hasNext()) {
                    byte[] right = (byte[]) iter.next();
                    tiger.reset();
                    tiger.update((byte) 1);
                    tiger.update(left, 0, left.length);
                    tiger.update(right, 0, right.length);
                    newNodes.add(tiger.digest());
                } else {
                    newNodes.add(left);
                }
            }
            nodes = newNodes;
        }
        System.arraycopy(nodes.get(0), 0, buf, offset, HASHSIZE);
        engineReset();
        return HASHSIZE;
    }
