    public byte[] computeHash(String algorithm) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(algorithm);
            if (eof) {
                digest.update(buffer, firstIndex, maxSize - firstIndex);
                digest.update(buffer, 0, firstIndex);
            } else {
                digest.update(buffer, 0, currentIndex);
            }
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            Logger.defaultLogger().error(e);
            return null;
        }
    }
