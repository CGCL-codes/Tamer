    public static final String updateDigest(final MessageDigest digest, final InputStream in) throws IOException {
        if ((null == digest) || (null == in)) throw new IOException("Missing digest arguments");
        final byte[] buffer = new byte[IOCopier.DEFAULT_COPY_SIZE];
        for (int rLen = in.read(buffer); rLen > 0; rLen = in.read(buffer)) digest.update(buffer, 0, rLen);
        final byte[] dValue = digest.digest();
        return Base64.encodeToString(dValue);
    }
