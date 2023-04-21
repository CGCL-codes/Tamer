    public Object sign() {
        if (!initSign) {
            throw new IllegalStateException();
        }
        final int k = (privkey.getModulus().bitLength() + 7) >>> 3;
        final byte[] d = Util.concat(md5.digest(), sha.digest());
        if (k - 11 < d.length) {
            throw new IllegalArgumentException("message too long");
        }
        final byte[] eb = new byte[k];
        eb[0] = 0x00;
        eb[1] = 0x01;
        for (int i = 2; i < k - d.length - 1; i++) {
            eb[i] = (byte) 0xFF;
        }
        System.arraycopy(d, 0, eb, k - d.length, d.length);
        BigInteger EB = new BigInteger(eb);
        BigInteger EM = RSA.sign(privkey, EB);
        return Util.trim(EM);
    }
