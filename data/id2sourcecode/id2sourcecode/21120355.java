    private void setupGlobalEncryptionKey(byte[] documentID, byte userPad[], byte ownerKey[], int permissions) {
        this.documentID = documentID;
        this.ownerKey = ownerKey;
        this.permissions = permissions;
        mkey = new byte[keyLength / 8];
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
        if (!encryptMetadata) md5.update(metadataPad);
        byte digest[] = new byte[mkey.length];
        System.arraycopy(md5.digest(), 0, digest, 0, mkey.length);
        if (revision == STANDARD_ENCRYPTION_128 || revision == AES_128) {
            for (int k = 0; k < 50; ++k) System.arraycopy(md5.digest(digest), 0, digest, 0, mkey.length);
        }
        System.arraycopy(digest, 0, mkey, 0, mkey.length);
    }
