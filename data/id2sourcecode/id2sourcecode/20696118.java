    private byte[] trimOrExpand(byte[] b, Map lengths, String alg, IMessageDigest hash, byte[] k, byte x, byte[] h) {
        Integer len = (Integer) lengths.get(alg);
        if (len == null) return b;
        int l = len.intValue();
        if (l == b.length) {
            return b;
        } else if (l < b.length) {
            byte[] buf = new byte[l];
            System.arraycopy(b, 0, buf, 0, l);
            return buf;
        }
        byte[][] bb = new byte[l / b.length + ((l % b.length == 0) ? 0 : 1)][];
        for (int i = 0; i < bb.length; i++) {
            hash.reset();
            hash.update(k, 0, k.length);
            hash.update(h, 0, h.length);
            if (i == 0) {
                hash.update(x);
                hash.update(client.session_id, 0, client.session_id.length);
            } else {
                for (int j = 0; j < i; j++) hash.update(bb[j], 0, bb[j].length);
            }
            bb[i] = hash.digest();
        }
        byte[] buf = new byte[bb.length * b.length];
        int off = 0;
        for (int i = 0; i < bb.length; i++) {
            System.arraycopy(bb[i], 0, buf, off, bb[i].length);
            off += bb[i].length;
        }
        if (buf.length > l) {
            byte[] buf2 = new byte[l];
            System.arraycopy(buf, 0, buf2, 0, l);
            return buf2;
        }
        return buf;
    }
