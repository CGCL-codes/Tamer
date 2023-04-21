    private void setupUserKey() {
        if (revision == STANDARD_ENCRYPTION_128 || revision == AES_128) {
            md5.update(pad);
            byte digest[] = md5.digest(documentID);
            System.arraycopy(digest, 0, userKey, 0, 16);
            for (int k = 16; k < 32; ++k) userKey[k] = 0;
            for (int i = 0; i < 20; ++i) {
                for (int j = 0; j < mkey.length; ++j) digest[j] = (byte) (mkey[j] ^ i);
                arcfour.prepareARCFOURKey(digest, 0, mkey.length);
                arcfour.encryptARCFOUR(userKey, 0, 16);
            }
        } else {
            arcfour.prepareARCFOURKey(mkey);
            arcfour.encryptARCFOUR(pad, userKey);
        }
    }
