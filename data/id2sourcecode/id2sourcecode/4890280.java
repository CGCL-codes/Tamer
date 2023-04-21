    private Parameters decodeSetup(JESMac jesMac, Algorithm algorithm, boolean limitedCryptography, byte[] header, byte[] dk, int maxmem, double maxmemfrac, double maxtime) throws GeneralSecurityException, Exception {
        byte[] salt = new byte[32];
        Parameters parameters = new Parameters();
        parameters.logN = header[7];
        parameters.r = be32dec(header, 8);
        parameters.p = be32dec(header, 12);
        parameters.N = 1 << parameters.logN;
        System.arraycopy(header, 16, salt, 0, 32);
        MessageDigest jesMessageDigest = JESMessageDigest.getInstance(jesMac.getAlgorithm().substring(4));
        jesMessageDigest.update(header, 0, 48);
        byte[] hbuf = jesMessageDigest.digest();
        byte[] computedDigest = new byte[16];
        System.arraycopy(hbuf, 0, computedDigest, 0, 16);
        byte[] storedDigest = new byte[16];
        System.arraycopy(header, 48, storedDigest, 0, 16);
        if (!Arrays.equals(storedDigest, computedDigest)) {
            throw new Exception("Input is not valid scrypt-encrypted block");
        }
        checkParams(jesMac.getAlgorithm(), maxmem, maxmemfrac, maxtime, parameters);
        cryptoScrypt(jesMac, salt, parameters, dk);
        int macLength = jesMac.getMacLength();
        byte[] keyHMAC = new byte[macLength];
        System.arraycopy(dk, limitedCryptography ? 16 : 32, keyHMAC, 0, macLength);
        jesMac.init(new SecretKeySpec(keyHMAC, algorithm.getHMACAlgorithm()), new HMACParameterSpec());
        jesMac.update(header, 0, 64);
        byte[] computedHMAC = jesMac.doFinal();
        byte[] storedHMAC = new byte[macLength];
        System.arraycopy(header, 64, storedHMAC, 0, macLength);
        if (!Arrays.equals(storedHMAC, computedHMAC)) {
            throw new Exception("Passphrase is incorrect");
        }
        return parameters;
    }
