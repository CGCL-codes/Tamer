    private void encrypt(InputStream in, OutputStream out, byte[] key, byte[] iv, boolean prependIv) throws CryptoException {
        if (prependIv && iv != null && iv.length > 0) {
            try {
                out.write(iv);
            } catch (IOException e) {
                throw new CryptoException(e);
            }
        }
        crypt(in, out, key, iv, javax.crypto.Cipher.ENCRYPT_MODE);
    }
