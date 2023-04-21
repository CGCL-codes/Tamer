    protected byte[] calcDigest() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(CCNDigestHelper.DEFAULT_DIGEST_ALGORITHM);
            DigestOutputStream dos = new DigestOutputStream(new NullOutputStream(), md);
            encode(dos);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (ContentEncodingException e) {
            throw new RuntimeException(e);
        }
        return md.digest();
    }
