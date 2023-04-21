    protected static SecretKey getKeyFromPassphrase(String passphrase, byte[] iv, int keySize) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] passphraseBytes;
        try {
            passphraseBytes = passphrase.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new Error("Mandatory US-ASCII character encoding is not supported by the VM");
        }
        MessageDigest hash = MessageDigest.getInstance("MD5");
        byte[] key = new byte[keySize];
        int hashesSize = keySize & 0xfffffff0;
        if ((keySize & 0xf) != 0) {
            hashesSize += MD5_HASH_BYTES;
        }
        byte[] hashes = new byte[hashesSize];
        byte[] previous;
        for (int index = 0; (index + MD5_HASH_BYTES) <= hashes.length; hash.update(previous, 0, previous.length)) {
            hash.update(passphraseBytes, 0, passphraseBytes.length);
            hash.update(iv, 0, iv.length);
            previous = hash.digest();
            System.arraycopy(previous, 0, hashes, index, previous.length);
            index += previous.length;
        }
        System.arraycopy(hashes, 0, key, 0, key.length);
        return new SecretKeySpec(key, "DESede");
    }
