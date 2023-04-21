    public void encryptData(long objectNumber, long genNumber, InputStream data, OutputStream output) throws CryptographyException, IOException {
        byte[] newKey = new byte[encryptionKey.length + 5];
        System.arraycopy(encryptionKey, 0, newKey, 0, encryptionKey.length);
        newKey[newKey.length - 5] = (byte) (objectNumber & 0xff);
        newKey[newKey.length - 4] = (byte) ((objectNumber >> 8) & 0xff);
        newKey[newKey.length - 3] = (byte) ((objectNumber >> 16) & 0xff);
        newKey[newKey.length - 2] = (byte) (genNumber & 0xff);
        newKey[newKey.length - 1] = (byte) ((genNumber >> 8) & 0xff);
        byte[] digestedKey = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            digestedKey = md.digest(newKey);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptographyException(e);
        }
        int length = Math.min(newKey.length, 16);
        byte[] finalKey = new byte[length];
        System.arraycopy(digestedKey, 0, finalKey, 0, length);
        rc4.setKey(finalKey);
        rc4.write(data, output);
        output.flush();
    }
