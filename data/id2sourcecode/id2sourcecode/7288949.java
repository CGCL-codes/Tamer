    private byte[] recover(byte[] protectedKey) throws UnrecoverableKeyException, NoSuchAlgorithmException {
        int i, j;
        byte[] digest;
        int numRounds;
        int xorOffset;
        int encrKeyLen;
        MessageDigest md = MessageDigest.getInstance("SHA");
        byte[] salt = new byte[SALT_LEN];
        System.arraycopy(protectedKey, 0, salt, 0, SALT_LEN);
        encrKeyLen = protectedKey.length - SALT_LEN - DIGEST_LEN;
        numRounds = encrKeyLen / DIGEST_LEN;
        if ((encrKeyLen % DIGEST_LEN) != 0) numRounds++;
        byte[] encrKey = new byte[encrKeyLen];
        System.arraycopy(protectedKey, SALT_LEN, encrKey, 0, encrKeyLen);
        byte[] xorKey = new byte[encrKey.length];
        byte[] passwdBytes = new byte[password.length * 2];
        for (i = 0, j = 0; i < password.length; i++) {
            passwdBytes[j++] = (byte) (password[i] >> 8);
            passwdBytes[j++] = (byte) password[i];
        }
        for (i = 0, xorOffset = 0, digest = salt; i < numRounds; i++, xorOffset += DIGEST_LEN) {
            md.update(passwdBytes);
            md.update(digest);
            digest = md.digest();
            md.reset();
            if (i < numRounds - 1) {
                System.arraycopy(digest, 0, xorKey, xorOffset, digest.length);
            } else {
                System.arraycopy(digest, 0, xorKey, xorOffset, xorKey.length - xorOffset);
            }
        }
        byte[] plainKey = new byte[encrKey.length];
        for (i = 0; i < plainKey.length; i++) {
            plainKey[i] = (byte) (encrKey[i] ^ xorKey[i]);
        }
        md.update(passwdBytes);
        java.util.Arrays.fill(passwdBytes, (byte) 0x00);
        passwdBytes = null;
        md.update(plainKey);
        digest = md.digest();
        md.reset();
        for (i = 0; i < digest.length; i++) {
            if (digest[i] != protectedKey[SALT_LEN + encrKeyLen + i]) {
                throw new UnrecoverableKeyException("Cannot recover key");
            }
        }
        return plainKey;
    }
