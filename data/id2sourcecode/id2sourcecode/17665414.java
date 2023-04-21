    private String getUniqueID(String account) throws Exception {
        byte[] bytes = account.getBytes();
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        byte[] digest = sha.digest(bytes);
        return System.currentTimeMillis() + hexEncode(digest);
    }
