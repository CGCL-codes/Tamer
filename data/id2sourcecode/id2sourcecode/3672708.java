    private void setupGlobalEncryptionKey(byte[] documentID, byte userPad[], byte ownerKey[], int permissions, boolean strength128Bits) {
        this.documentID = documentID;
        this.ownerKey = ownerKey;
        this.permissions = permissions;
        mkey = new byte[strength128Bits ? 16 : 5];
        md5.reset();
        md5.update(userPad);
        md5.update(ownerKey);
        byte ext[] = new byte[4];
        ext[0] = (byte) permissions;
        ext[1] = (byte) (permissions >> 8);
        ext[2] = (byte) (permissions >> 16);
        ext[3] = (byte) (permissions >> 24);
        md5.update(ext, 0, 4);
        if (documentID != null) md5.update(documentID);
        byte digest[] = md5.digest();
        if (mkey.length == 16) {
            for (int k = 0; k < 50; ++k) digest = md5.digest(digest);
        }
        System.arraycopy(digest, 0, mkey, 0, mkey.length);
    }
