    @Override
    protected byte[] createCryptKey(byte[] password) throws COSSecurityException {
        try {
            MessageDigest md = MessageDigest.getInstance(DIGEST_ALGORITHM);
            byte[] prepared = prepareBytes(password);
            md.update(prepared);
            md.update(getO());
            md.update(getPBytes());
            byte[] fd = getPermanentFileID();
            if (fd != null) {
                md.update(fd);
            }
            if (!isEncryptMetadata()) {
                md.update(HIGH_BYTES);
            }
            byte[] key = md.digest();
            int length = 16;
            for (int i = 0; i < 50; i++) {
                md.update(key, 0, length);
                key = md.digest();
            }
            byte[] result = new byte[length];
            System.arraycopy(key, 0, result, 0, length);
            return result;
        } catch (Exception e) {
            throw new COSSecurityException(e);
        }
    }
