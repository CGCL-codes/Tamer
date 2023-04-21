    public byte[] getM2(byte[] s, byte[] B) {
        byte[] A = get_A();
        byte[] M = getM1(s, B);
        byte[] K = get_K(get_S(s, B));
        MessageDigest M2 = getSHA1();
        M2.update(A);
        M2.update(M);
        M2.update(K);
        return M2.digest();
    }
