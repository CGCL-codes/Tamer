    public void setupAllKeys(byte userPassword[], byte ownerPassword[], int permissions, boolean strength128Bits) {
        if (ownerPassword == null || ownerPassword.length == 0) ownerPassword = md5.digest(createDocumentId());
        permissions |= strength128Bits ? 0xfffff0c0 : 0xffffffc0;
        permissions &= 0xfffffffc;
        byte userPad[] = padPassword(userPassword);
        byte ownerPad[] = padPassword(ownerPassword);
        this.ownerKey = computeOwnerKey(userPad, ownerPad, strength128Bits);
        documentID = createDocumentId();
        setupByUserPad(this.documentID, userPad, this.ownerKey, permissions, strength128Bits);
    }
