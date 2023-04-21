    private void setupUserKey() {
        if (mkey.length == 16) {
            md5.update(pad);
            byte digest[] = md5.digest(documentID);
            System.arraycopy(digest, 0, userKey, 0, 16);
            for (int k = 16; k < 32; ++k) userKey[k] = 0;
            for (int i = 0; i < 20; ++i) {
                for (int j = 0; j < mkey.length; ++j) digest[j] = (byte) (mkey[j] ^ i);
                prepareRC4Key(digest, 0, mkey.length);
                encryptRC4(userKey, 0, 16);
            }
        } else {
            prepareRC4Key(mkey);
            encryptRC4(pad, userKey);
        }
    }
