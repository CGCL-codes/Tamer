    private byte[] calculateFingerprintOfKey(BigInteger iMod, BigInteger iExp, int iModLen, int iExpLen, byte[] iDate) throws OpenPGPCardException {
        try {
            int modLen = (int) Math.ceil((double) iMod.bitLength() / 8);
            int expLen = (int) Math.ceil((double) iExp.bitLength() / 8);
            int n = 6 + 2 + modLen + 2 + expLen;
            int nbits;
            byte[] data;
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            buf.write(0x99);
            buf.write(n >> 8);
            buf.write(n & 0xFF);
            buf.write(4);
            buf.write(iDate, 0, 4);
            buf.write(ALGO_RSA);
            nbits = iMod.bitLength();
            buf.write(nbits >> 8);
            buf.write(nbits & 0xFF);
            data = iMod.toByteArray();
            buf.write(data, data.length - modLen, modLen);
            nbits = iExp.bitLength();
            buf.write(nbits >> 8);
            buf.write(nbits & 0xFF);
            data = iExp.toByteArray();
            buf.write(data, data.length - expLen, expLen);
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            return sha1.digest(buf.toByteArray());
        } catch (NoSuchAlgorithmException ex) {
            throw new OpenPGPCardException("Could not get an instance for SHA1 MessageDigest");
        }
    }
