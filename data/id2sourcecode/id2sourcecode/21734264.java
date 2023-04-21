    public static String encrypt(String string) {
        byte[] hash = null;
        try {
            hash = MessageDigest.getInstance(ALGORITHM).digest(string.getBytes(ENCODING));
        } catch (NoSuchAlgorithmException err) {
            throw new Error("no " + ALGORITHM + " support in this VM");
        } catch (UnsupportedEncodingException err) {
            throw new Error("no " + ENCODING + " support in this VM");
        }
        return getHashString(hash);
    }
