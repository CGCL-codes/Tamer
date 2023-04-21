    private String Digest() {
        String Digest = new String();
        String A1, A2;
        if (user.length() == 0 || pass.length() == 0 || realm.length() == 0 || file.length() == 0 || nonce.length() == 0 || cnonce.length() == 0 || method.length() == 0) return Digest;
        A1 = getA1();
        A2 = getA2();
        MD.reset();
        if (qop == null) Digest = A1 + ":" + nonce + ":" + A2; else Digest = A1 + ":" + nonce + ":" + count + ":" + cnonce + ":" + qop + ":" + A2;
        MD.update(Digest.getBytes());
        Digest = HexString.convert(MD.digest(), 16);
        return Digest;
    }
