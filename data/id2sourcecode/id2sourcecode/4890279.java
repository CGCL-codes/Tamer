    private void encodeSetup(JESMac jesMac, Algorithm algorithm, boolean limitedCryptography, byte[] header, byte[] dk, int maxmem, double maxmemfrac, double maxtime) throws GeneralSecurityException, Exception {
        byte[] salt = new byte[32];
        Parameters parameters = pickParams(jesMac.getAlgorithm(), maxmem, maxmemfrac, maxtime);
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.nextBytes(salt);
        cryptoScrypt(jesMac, salt, parameters, dk);
        System.arraycopy("scrypt".getBytes(), 0, header, 0, 6);
        header[6] = (byte) (algorithm.getHMACAlgorithmCode() | (limitedCryptography ? 0x40 : 0x00));
        header[7] = parameters.logN;
        be32enc(header, 8, parameters.r);
        be32enc(header, 12, parameters.p);
        System.arraycopy(salt, 0, header, 16, 32);
        MessageDigest jesMessageDigest = JESMessageDigest.getInstance(jesMac.getAlgorithm().substring(4));
        jesMessageDigest.update(header, 0, 48);
        byte[] hbuf = jesMessageDigest.digest();
        System.arraycopy(hbuf, 0, header, 48, 16);
        int macLength = jesMac.getMacLength();
        byte[] keyHMAC = new byte[macLength];
        System.arraycopy(dk, limitedCryptography ? 16 : 32, keyHMAC, 0, macLength);
        jesMac.init(new SecretKeySpec(keyHMAC, algorithm.getHMACAlgorithm()), new HMACParameterSpec());
        jesMac.update(header, 0, 64);
        hbuf = jesMac.doFinal();
        System.arraycopy(hbuf, 0, header, 64, macLength);
    }
