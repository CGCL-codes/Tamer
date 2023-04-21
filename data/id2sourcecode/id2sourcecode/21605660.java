    public static EgovFormBasedUUID nameUUIDFromBytes(byte[] name) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException nsae) {
            throw new InternalError("SHA-256 not supported");
        }
        if (md == null) {
            throw new RuntimeException("MessageDigest is null!!");
        }
        byte[] sha = md.digest(name);
        byte[] md5Bytes = new byte[8];
        System.arraycopy(sha, 0, md5Bytes, 0, 8);
        md5Bytes[6] &= 0x0f;
        md5Bytes[6] |= 0x30;
        md5Bytes[8] &= 0x3f;
        md5Bytes[8] |= 0x80;
        return new EgovFormBasedUUID(md5Bytes);
    }
