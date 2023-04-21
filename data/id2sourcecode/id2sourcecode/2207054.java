    public boolean verify(Object signature) {
        if (!initVerify) {
            throw new IllegalStateException();
        }
        BigInteger EM = new BigInteger(1, (byte[]) signature);
        BigInteger EB = RSA.verify(pubkey, EM);
        int i = 0;
        final byte[] eb = EB.toByteArray();
        if (eb[0] == 0x00) {
            for (i = 0; i < eb.length && eb[i] == 0x00; i++) ;
        } else if (eb[0] == 0x01) {
            for (i = 1; i < eb.length && eb[i] != 0x00; i++) {
                if (eb[i] != (byte) 0xFF) {
                    throw new IllegalArgumentException("bad padding");
                }
            }
            i++;
        } else {
            throw new IllegalArgumentException("decryption failed");
        }
        byte[] d1 = Util.trim(eb, i, eb.length - i);
        byte[] d2 = Util.concat(md5.digest(), sha.digest());
        return Arrays.equals(d1, d2);
    }
