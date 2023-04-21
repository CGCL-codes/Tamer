    private byte[] getPassHash(MessageDigest md5, ByteBlock key, ByteBlock passBytes) {
        md5.update(key.toByteArray());
        md5.update(passBytes.toByteArray());
        md5.update(AIMSM_BYTES);
        return md5.digest();
    }
