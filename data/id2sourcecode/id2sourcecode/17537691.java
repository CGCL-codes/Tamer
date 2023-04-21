    @Override
    protected synchronized byte[] basicEncrypt(byte[] data, byte[] encryptionKey, int objectNum, int genNum) throws COSSecurityException {
        try {
            updateHash(encryptionKey, objectNum, genNum);
            byte[] keyBase = md.digest();
            byte[] initVector = cipher.getIV();
            if (initVector == null) {
                initVector = new byte[16];
            }
            IvParameterSpec ivSpec = new IvParameterSpec(initVector, 0, initVector.length);
            SecretKey skeySpec = new SecretKeySpec(keyBase, 0, length, KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(data, 0, data.length);
            byte[] result = new byte[initVector.length + encrypted.length];
            System.arraycopy(initVector, 0, result, 0, initVector.length);
            System.arraycopy(encrypted, 0, result, initVector.length, encrypted.length);
            return result;
        } catch (Exception e) {
            throw new COSSecurityException(e);
        }
    }
