    private SecretKeySpec getSecretKey(byte[] pwd, byte[] iv) throws GeneralSecurityException {
        byte[] key = new byte[this.keyLength];
        int offset = 0;
        int bytesNeeded = this.keyLength;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        for (; ; ) {
            md5.update(pwd);
            md5.update(iv, 0, 8);
            byte[] b = md5.digest();
            int len = (bytesNeeded > b.length) ? b.length : bytesNeeded;
            System.arraycopy(b, 0, key, offset, len);
            offset += len;
            bytesNeeded = key.length - offset;
            if (bytesNeeded == 0) {
                break;
            }
            md5.reset();
            md5.update(b);
        }
        return new SecretKeySpec(key, this.encAlg);
    }
