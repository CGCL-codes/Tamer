    private byte[] computeOwnerKey(byte userPad[], byte ownerPad[], boolean strength128Bits) {
        byte ownerKey[] = new byte[32];
        byte digest[] = md5.digest(ownerPad);
        if (strength128Bits) {
            byte mkey[] = new byte[16];
            for (int k = 0; k < 50; ++k) digest = md5.digest(digest);
            System.arraycopy(userPad, 0, ownerKey, 0, 32);
            for (int i = 0; i < 20; ++i) {
                for (int j = 0; j < mkey.length; ++j) mkey[j] = (byte) (digest[j] ^ i);
                prepareRC4Key(mkey);
                encryptRC4(ownerKey);
            }
        } else {
            prepareRC4Key(digest, 0, 5);
            encryptRC4(userPad, ownerKey);
        }
        return ownerKey;
    }
