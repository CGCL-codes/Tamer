    private byte[] encryptPassword(String pass, ByteBlock key, boolean hashedPass) {
        byte[] passBytes = BinaryTools.getAsciiBytes(pass);
        if (hashedPass) {
            MessageDigest digest = createMd5Hasher();
            passBytes = digest.digest(passBytes);
            return getPassHash(digest, key, ByteBlock.wrap(passBytes));
        } else {
            return getPassHash(key, ByteBlock.wrap(passBytes));
        }
    }
