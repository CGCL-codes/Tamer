    private byte[] digest(byte[] data, String method) throws DigestException {
        try {
            MessageDigest md = MessageDigest.getInstance(method);
            md.update(data);
            byte[] toDigest = md.digest();
            return toDigest;
        } catch (NoSuchAlgorithmException ne) {
            throw new DigestException("couldn't make digest due to the wrong digest method");
        }
    }
