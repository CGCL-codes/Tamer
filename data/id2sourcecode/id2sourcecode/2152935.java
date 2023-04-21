    public byte[] getM1(byte[] s, byte[] B) {
        MessageDigest totalCtx = getSHA1();
        totalCtx.update(I);
        totalCtx.update(getSHA1().digest(username.getBytes()));
        totalCtx.update(s);
        totalCtx.update(get_A());
        totalCtx.update(B);
        totalCtx.update(get_K(get_S(s, B)));
        return totalCtx.digest();
    }
