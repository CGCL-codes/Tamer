    public void init() {
        byte[] oValue;
        if (ownerPassword.length() > 0) {
            oValue = encryptWithHash(prepPassword(userPassword), prepPassword(ownerPassword), 5);
        } else {
            oValue = encryptWithHash(prepPassword(userPassword), prepPassword(userPassword), 5);
        }
        int permissions = -4;
        if (!allowPrint) {
            permissions -= PERMISSION_PRINT;
        }
        if (!allowCopyContent) {
            permissions -= PERMISSION_COPY_CONTENT;
        }
        if (!allowEditContent) {
            permissions -= PERMISSION_EDIT_CONTENT;
        }
        if (!allowEditAnnotations) {
            permissions -= PERMISSION_EDIT_ANNOTATIONS;
        }
        digest.update(prepPassword(userPassword));
        digest.update(oValue);
        digest.update((byte) (permissions >>> 0));
        digest.update((byte) (permissions >>> 8));
        digest.update((byte) (permissions >>> 16));
        digest.update((byte) (permissions >>> 24));
        digest.update(getFileID());
        byte[] hash = digest.digest();
        this.encryptionKey = new byte[5];
        for (int i = 0; i < 5; i++) {
            this.encryptionKey[i] = hash[i];
        }
        byte[] uValue = encryptWithKey(prepPassword(""), this.encryptionKey);
        this.dictionary = this.number + " " + this.generation + " obj\n<< /Filter /Standard\n" + "/V 1" + "/R 2" + "/Length 40" + "/P " + permissions + "\n" + "/O <" + toHex(oValue) + ">\n" + "/U <" + toHex(uValue) + ">\n" + ">>\n" + "endobj\n";
    }
