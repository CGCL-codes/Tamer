    public void digest(byte[] out, int outOffset) {
        if (outOffset < 0 || outOffset + tagSize > out.length) throw new IndexOutOfBoundsException();
        byte[] N = nonceOmac.digest();
        byte[] H = headerOmac.digest();
        byte[] M = msgOmac.digest();
        for (int i = 0; i < tagSize; i++) out[outOffset + i] = (byte) (N[i] ^ H[i] ^ M[i]);
        reset();
    }
