    public String encrypt(String string) {
        byte[] hash = null;
        try {
            hash = MessageDigest.getInstance(getAlgorithm()).digest(string.getBytes(getEncoding()));
        } catch (NoSuchAlgorithmException err) {
            throw new Error("no " + getAlgorithm() + " support in this VM");
        } catch (UnsupportedEncodingException err) {
            throw new Error("no " + getAlgorithm() + " support in this VM");
        }
        return getHashString(hash);
    }
