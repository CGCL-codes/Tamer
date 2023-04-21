    public final Key getKey(String s) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, CloneNotSupportedException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        MessageDigest md2;
        byte[] km = new byte[keyLength], pw = s.getBytes();
        int i, l = md.getDigestLength(), r = keyLength - l;
        for (i = 0; i < r; i += l) {
            md2 = (MessageDigest) md.clone();
            md.update((byte) 0);
            md2.update(salt);
            md2.update(pw);
            System.arraycopy(md2.digest(), 0, km, i, l);
        }
        md.update(salt);
        md.update(pw);
        System.arraycopy(md.digest(), 0, km, i, keyLength - i);
        return makeKey(km);
    }
