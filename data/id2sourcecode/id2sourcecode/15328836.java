    private byte[] getObjectSaltedDecryptionKey(int objNum, int objGen) throws PDFParseException {
        byte[] decryptionKeyBytes;
        final MessageDigest md5;
        try {
            md5 = createMD5Digest();
        } catch (NoSuchAlgorithmException e) {
            throw new PDFParseException("Unable to get MD5 digester", e);
        }
        md5.update(this.generalKeyBytes);
        md5.update((byte) objNum);
        md5.update((byte) (objNum >> 8));
        md5.update((byte) (objNum >> 16));
        md5.update((byte) objGen);
        md5.update((byte) (objGen >> 8));
        if (encryptionAlgorithm == EncryptionAlgorithm.AESV2) {
            md5.update(AESV2_SALT);
        }
        final byte[] hash = md5.digest();
        final int keyLen = getSaltedContentKeyByteLength();
        decryptionKeyBytes = new byte[keyLen];
        System.arraycopy(hash, 0, decryptionKeyBytes, 0, keyLen);
        return decryptionKeyBytes;
    }
