    public String getThumbprint() throws NoSuchAlgorithmException {
        byte[] token = getToken();
        MessageDigest digest = MessageDigest.getInstance("SHA");
        digest.update(token);
        byte[] thumbPrintBytes = digest.digest();
        return Base64.encode(thumbPrintBytes);
    }
