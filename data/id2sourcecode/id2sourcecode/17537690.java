    @Override
    protected synchronized byte[] basicDecrypt(byte[] data, byte[] encryptionKey, int objectNum, int genNum) throws COSSecurityException {
        try {
            updateHash(encryptionKey, objectNum, genNum);
            byte[] keyBase = md.digest();
            IvParameterSpec ivSpec = new IvParameterSpec(data, 0, blockSize);
            SecretKey skeySpec = new SecretKeySpec(keyBase, 0, length, KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
            return cipher.doFinal(data, blockSize, data.length - blockSize);
        } catch (Exception e) {
            throw new COSSecurityException(e);
        }
    }
