    public void digest(byte[] buf, int offset, int len) throws DigestException {
        try {
            messageDigest.digest(buf, offset, len);
        } catch (com.sun.midp.crypto.DigestException e) {
            throw new DigestException(e.getMessage());
        }
    }
