    public static SecretKey deriveKey(byte[] keySeed, int mode) throws GeneralSecurityException {
        MessageDigest shaDigest = MessageDigest.getInstance("SHA1");
        shaDigest.update(keySeed);
        byte[] c = { 0x00, 0x00, 0x00, (byte) mode };
        shaDigest.update(c);
        byte[] hash = shaDigest.digest();
        byte[] key = new byte[24];
        System.arraycopy(hash, 0, key, 0, 8);
        System.arraycopy(hash, 8, key, 8, 8);
        System.arraycopy(hash, 0, key, 16, 8);
        SecretKeyFactory desKeyFactory = SecretKeyFactory.getInstance("DESede");
        return desKeyFactory.generateSecret(new DESedeKeySpec(key));
    }
