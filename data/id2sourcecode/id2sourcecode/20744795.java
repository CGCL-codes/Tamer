    public BigIntegerEx get_x(byte[] salt) {
        MessageDigest mdx = getSHA1();
        mdx.update(username.getBytes());
        mdx.update(":".getBytes());
        mdx.update(password.getBytes());
        byte[] hash = mdx.digest();
        mdx = getSHA1();
        mdx.update(salt);
        mdx.update(hash);
        hash = mdx.digest();
        return new BigIntegerEx(BigIntegerEx.LITTLE_ENDIAN, hash);
    }
