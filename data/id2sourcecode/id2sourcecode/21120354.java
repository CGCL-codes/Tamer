    private byte[] computeOwnerKey(byte userPad[], byte ownerPad[]) {
        byte ownerKey[] = new byte[32];
        byte digest[] = md5.digest(ownerPad);
        if (revision == STANDARD_ENCRYPTION_128 || revision == AES_128) {
            byte mkey[] = new byte[keyLength / 8];
            for (int k = 0; k < 50; ++k) {
                md5.update(digest, 0, mkey.length);
                System.arraycopy(md5.digest(), 0, digest, 0, mkey.length);
            }
            System.arraycopy(userPad, 0, ownerKey, 0, 32);
            for (int i = 0; i < 20; ++i) {
                for (int j = 0; j < mkey.length; ++j) mkey[j] = (byte) (digest[j] ^ i);
                arcfour.prepareARCFOURKey(mkey);
                arcfour.encryptARCFOUR(ownerKey);
            }
        } else {
            arcfour.prepareARCFOURKey(digest, 0, 5);
            arcfour.encryptARCFOUR(userPad, ownerKey);
        }
        return ownerKey;
    }
