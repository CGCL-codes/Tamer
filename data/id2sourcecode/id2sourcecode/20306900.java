    private static byte[] encrypt(boolean encrypt, byte[] key, byte[] input) throws CryptoException {
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(Security.createEngine());
        cipher.init(encrypt, new KeyParameter(Security.digest(key)));
        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
        int outputLen = cipher.processBytes(input, 0, input.length, cipherText, 0);
        cipher.doFinal(cipherText, outputLen);
        return cipherText;
    }
