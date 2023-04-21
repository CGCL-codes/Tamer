    private byte[] MGF(byte[] Z, int l) {
        if (l < 1 || (l & 0xFFFFFFFFL) > ((hLen & 0xFFFFFFFFL) << 32L)) throw new IllegalArgumentException("mask too long");
        byte[] result = new byte[l];
        int limit = ((l + hLen - 1) / hLen) - 1;
        IMessageDigest hashZ = null;
        hashZ = (IMessageDigest) hash.clone();
        hashZ.digest();
        hashZ.update(Z, 0, Z.length);
        IMessageDigest hashZC = null;
        byte[] t;
        int sofar = 0;
        int length;
        for (int i = 0; i < limit; i++) {
            hashZC = (IMessageDigest) hashZ.clone();
            hashZC.update((byte) (i >>> 24));
            hashZC.update((byte) (i >>> 16));
            hashZC.update((byte) (i >>> 8));
            hashZC.update((byte) i);
            t = hashZC.digest();
            length = l - sofar;
            length = (length > hLen ? hLen : length);
            System.arraycopy(t, 0, result, sofar, length);
            sofar += length;
        }
        return result;
    }
