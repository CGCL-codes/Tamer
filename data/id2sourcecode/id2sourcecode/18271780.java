    public byte[] encryptionKeyAlgorithm(String password, int keyLength) {
        byte[] paddedPassword = padPassword(password);
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            logger.log(Level.FINE, "NoSuchAlgorithmException.", ex);
        }
        md5.update(paddedPassword);
        String tmp = encryptionDictionary.getBigO();
        byte[] bigO = new byte[tmp.length()];
        for (int i = 0; i < tmp.length(); i++) {
            bigO[i] = (byte) tmp.charAt(i);
        }
        md5.update(bigO);
        for (int i = 0, p = encryptionDictionary.getPermissions(); i < 4; i++, p >>= 8) {
            md5.update((byte) p);
        }
        String firstFileID = ((StringObject) encryptionDictionary.getFileID().elementAt(0)).getLiteralString();
        byte[] fileID = new byte[firstFileID.length()];
        for (int i = 0; i < firstFileID.length(); i++) {
            fileID[i] = (byte) firstFileID.charAt(i);
        }
        paddedPassword = md5.digest(fileID);
        if (encryptionDictionary.getRevisionNumber() == 3) {
            for (int i = 0; i < 50; i++) {
                paddedPassword = md5.digest(paddedPassword);
            }
        }
        byte[] out = null;
        int n = 5;
        if (encryptionDictionary.getRevisionNumber() == 2) {
            out = new byte[n];
        } else if (encryptionDictionary.getRevisionNumber() == 3) {
            n = keyLength / 8;
            out = new byte[n];
        }
        System.arraycopy(paddedPassword, 0, out, 0, n);
        encryptionKey = out;
        return out;
    }
