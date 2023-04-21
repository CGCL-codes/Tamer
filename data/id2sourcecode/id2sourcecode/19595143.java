    public void setupAllKeys(byte userPassword[], byte ownerPassword[], int permissions) {
        if (ownerPassword == null || ownerPassword.length == 0) ownerPassword = md5.digest(createDocumentId());
        permissions |= (revision == STANDARD_ENCRYPTION_128 || revision == AES_128) ? 0xfffff0c0 : 0xffffffc0;
        permissions &= 0xfffffffc;
        byte userPad[] = padPassword(userPassword);
        byte ownerPad[] = padPassword(ownerPassword);
        this.ownerKey = computeOwnerKey(userPad, ownerPad);
        documentID = createDocumentId();
        setupByUserPad(this.documentID, userPad, this.ownerKey, permissions);
    }
