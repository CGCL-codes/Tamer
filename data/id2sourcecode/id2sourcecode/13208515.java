    public boolean validate(byte[] saltData, byte[] saltHash) {
        check16Bytes(saltData, "saltData");
        check16Bytes(saltHash, "saltHash");
        RC4 rc4 = createRC4(0);
        byte[] saltDataPrime = saltData.clone();
        rc4.encrypt(saltDataPrime);
        byte[] saltHashPrime = saltHash.clone();
        rc4.encrypt(saltHashPrime);
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md5.update(saltDataPrime);
        byte[] finalSaltResult = md5.digest();
        if (false) {
            byte[] saltHashThatWouldWork = xor(saltHash, xor(saltHashPrime, finalSaltResult));
            System.out.println(HexDump.toHex(saltHashThatWouldWork));
        }
        return Arrays.equals(saltHashPrime, finalSaltResult);
    }
