    private static String processString(String sSource, String sKey, boolean bEncrypt) throws EncryptionException {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        String sResult = null;
        Cipher writeCipher = null;
        try {
            writeCipher = Cipher.getInstance(TRANSFORMATION);
            Key passKey = generateKey(sKey);
            int iMode = Cipher.DECRYPT_MODE;
            if (bEncrypt) {
                iMode = Cipher.ENCRYPT_MODE;
            }
            writeCipher.init(iMode, passKey);
            byte[] bytes = writeCipher.doFinal(sSource.getBytes());
            sResult = new String(bytes);
        } catch (Throwable t) {
            if (bEncrypt) {
                throw new EncryptionException("ENCRYPTION_EXCEPTION", t);
            } else {
                throw new EncryptionException("DECRYPTION_EXCEPTION", t);
            }
        }
        return sResult;
    }
