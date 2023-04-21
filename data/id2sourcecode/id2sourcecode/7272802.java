    public static KUID toKUID(GUID guid) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(guid.bytes());
            byte[] digest = md.digest();
            return KUID.createWithBytes(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
